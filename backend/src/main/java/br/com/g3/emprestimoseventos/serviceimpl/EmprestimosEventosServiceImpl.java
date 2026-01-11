package br.com.g3.emprestimoseventos.serviceimpl;

import br.com.g3.emprestimoseventos.domain.EmprestimoEvento;
import br.com.g3.emprestimoseventos.domain.EmprestimoEventoItem;
import br.com.g3.emprestimoseventos.domain.EmprestimoEventoMovimentacao;
import br.com.g3.emprestimoseventos.domain.EventoEmprestimo;
import br.com.g3.emprestimoseventos.dto.AgendaDiaDetalheResponse;
import br.com.g3.emprestimoseventos.dto.AgendaResumoDiaResponse;
import br.com.g3.emprestimoseventos.dto.ConflitoEmprestimoItemResponse;
import br.com.g3.emprestimoseventos.dto.DisponibilidadeItemResponse;
import br.com.g3.emprestimoseventos.dto.EmprestimoEventoItemRequest;
import br.com.g3.emprestimoseventos.dto.EmprestimoEventoItemResponse;
import br.com.g3.emprestimoseventos.dto.EmprestimoEventoListaResponse;
import br.com.g3.emprestimoseventos.dto.EmprestimoEventoMovimentacaoListaResponse;
import br.com.g3.emprestimoseventos.dto.EmprestimoEventoMovimentacaoResponse;
import br.com.g3.emprestimoseventos.dto.EmprestimoEventoRequest;
import br.com.g3.emprestimoseventos.dto.EmprestimoEventoResponse;
import br.com.g3.emprestimoseventos.dto.EventoEmprestimoRequest;
import br.com.g3.emprestimoseventos.dto.EventoEmprestimoResponse;
import br.com.g3.emprestimoseventos.dto.ResponsavelResumoResponse;
import br.com.g3.emprestimoseventos.mapper.EmprestimosEventosMapper;
import br.com.g3.emprestimoseventos.repository.EmprestimoEventoItemRepository;
import br.com.g3.emprestimoseventos.repository.EmprestimoEventoMovimentacaoRepository;
import br.com.g3.emprestimoseventos.repository.EmprestimoEventoRepository;
import br.com.g3.emprestimoseventos.repository.EventoEmprestimoRepository;
import br.com.g3.emprestimoseventos.repositoryimpl.EmprestimoEventoAgendaRegistro;
import br.com.g3.emprestimoseventos.repositoryimpl.EmprestimoEventoAgendaRow;
import br.com.g3.emprestimoseventos.repositoryimpl.EmprestimoEventoDisponibilidadeResumo;
import br.com.g3.emprestimoseventos.service.EmprestimosEventosService;
import br.com.g3.usuario.domain.Usuario;
import br.com.g3.usuario.repository.UsuarioRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EmprestimosEventosServiceImpl implements EmprestimosEventosService {
  private static final String STATUS_AGENDADO = "AGENDADO";
  private static final String STATUS_RETIRADO = "RETIRADO";
  private static final String STATUS_DEVOLVIDO = "DEVOLVIDO";
  private static final String STATUS_CANCELADO = "CANCELADO";
  private static final String STATUS_RASCUNHO = "RASCUNHO";

  private final EventoEmprestimoRepository eventoRepository;
  private final EmprestimoEventoRepository emprestimoRepository;
  private final EmprestimoEventoItemRepository itemRepository;
  private final EmprestimoEventoMovimentacaoRepository movimentacaoRepository;
  private final UsuarioRepository usuarioRepository;
  private final JdbcTemplate jdbcTemplate;
  private final EmprestimosEventosMapper mapper = new EmprestimosEventosMapper();

  public EmprestimosEventosServiceImpl(
      EventoEmprestimoRepository eventoRepository,
      EmprestimoEventoRepository emprestimoRepository,
      EmprestimoEventoItemRepository itemRepository,
      EmprestimoEventoMovimentacaoRepository movimentacaoRepository,
      UsuarioRepository usuarioRepository,
      JdbcTemplate jdbcTemplate) {
    this.eventoRepository = eventoRepository;
    this.emprestimoRepository = emprestimoRepository;
    this.itemRepository = itemRepository;
    this.movimentacaoRepository = movimentacaoRepository;
    this.usuarioRepository = usuarioRepository;
    this.jdbcTemplate = jdbcTemplate;
  }

  @Override
  @Transactional(readOnly = true)
  public EmprestimoEventoListaResponse listarEmprestimos(
      LocalDateTime inicio,
      LocalDateTime fim,
      String status,
      Long eventoId,
      Long itemId,
      Long unidadeId) {
    List<EmprestimoEvento> emprestimos =
        emprestimoRepository.listarPorFiltros(inicio, fim, status, eventoId, itemId, unidadeId);
    List<EmprestimoEventoResponse> respostas = new ArrayList<>();
    for (EmprestimoEvento emprestimo : emprestimos) {
      respostas.add(mapearEmprestimo(emprestimo));
    }
    return new EmprestimoEventoListaResponse(respostas);
  }

  @Override
  public EmprestimoEventoResponse obterEmprestimo(Long id) {
    EmprestimoEvento emprestimo = buscarEmprestimo(id);
    return mapearEmprestimo(emprestimo);
  }

  @Override
  @Transactional
  public EmprestimoEventoResponse criarEmprestimo(EmprestimoEventoRequest requisicao) {
    validarEmprestimoRequisicao(requisicao);
    EventoEmprestimo evento = buscarEvento(requisicao.getEventoId());

    EmprestimoEvento emprestimo = new EmprestimoEvento();
    aplicarRequisicao(emprestimo, requisicao, evento);
    LocalDateTime now = LocalDateTime.now();
    emprestimo.setCriadoEm(now);
    emprestimo.setAtualizadoEm(now);

    validarDisponibilidadeItens(requisicao.getItens(), requisicao.getDataRetiradaPrevista(),
        requisicao.getDataDevolucaoPrevista(), null);

    EmprestimoEvento salvo = emprestimoRepository.salvar(emprestimo);
    List<EmprestimoEventoItem> itensSalvos = salvarItens(salvo, requisicao.getItens());

    registrarMovimentacao(salvo, "CRIACAO", "Emprestimo criado", requisicao.getResponsavelId());
    return mapearEmprestimo(salvo, itensSalvos);
  }

  @Override
  @Transactional
  public EmprestimoEventoResponse atualizarEmprestimo(Long id, EmprestimoEventoRequest requisicao) {
    validarEmprestimoRequisicao(requisicao);
    EmprestimoEvento emprestimo = buscarEmprestimo(id);
    EventoEmprestimo evento = buscarEvento(requisicao.getEventoId());

    validarDisponibilidadeItens(
        requisicao.getItens(),
        requisicao.getDataRetiradaPrevista(),
        requisicao.getDataDevolucaoPrevista(),
        id);

    aplicarRequisicao(emprestimo, requisicao, evento);
    emprestimo.setAtualizadoEm(LocalDateTime.now());
    EmprestimoEvento salvo = emprestimoRepository.salvar(emprestimo);

    itemRepository.removerPorEmprestimoId(salvo.getId());
    List<EmprestimoEventoItem> itensSalvos = salvarItens(salvo, requisicao.getItens());
    registrarMovimentacao(salvo, "ATUALIZACAO", "Emprestimo atualizado", requisicao.getResponsavelId());
    return mapearEmprestimo(salvo, itensSalvos);
  }

  @Override
  @Transactional
  public void excluirEmprestimo(Long id) {
    EmprestimoEvento emprestimo = buscarEmprestimo(id);
    itemRepository.removerPorEmprestimoId(id);
    emprestimo.setStatus(STATUS_CANCELADO);
    emprestimo.setAtualizadoEm(LocalDateTime.now());
    emprestimoRepository.salvar(emprestimo);
  }

  @Override
  @Transactional
  public EmprestimoEventoResponse confirmarRetirada(Long id, Long usuarioId) {
    EmprestimoEvento emprestimo = buscarEmprestimo(id);
    emprestimo.setDataRetiradaReal(LocalDateTime.now());
    emprestimo.setStatus(STATUS_RETIRADO);
    emprestimo.setAtualizadoEm(LocalDateTime.now());
    EmprestimoEvento salvo = emprestimoRepository.salvar(emprestimo);
    List<EmprestimoEventoItem> itens = atualizarStatusItens(salvo.getId(), STATUS_RETIRADO);
    registrarMovimentacao(salvo, "RETIRADA", "Retirada confirmada", usuarioId);
    return mapearEmprestimo(salvo, itens);
  }

  @Override
  @Transactional
  public EmprestimoEventoResponse confirmarDevolucao(Long id, Long usuarioId) {
    EmprestimoEvento emprestimo = buscarEmprestimo(id);
    emprestimo.setDataDevolucaoReal(LocalDateTime.now());
    emprestimo.setStatus(STATUS_DEVOLVIDO);
    emprestimo.setAtualizadoEm(LocalDateTime.now());
    EmprestimoEvento salvo = emprestimoRepository.salvar(emprestimo);
    List<EmprestimoEventoItem> itens = atualizarStatusItens(salvo.getId(), STATUS_DEVOLVIDO);
    registrarMovimentacao(salvo, "DEVOLUCAO", "Devolucao confirmada", usuarioId);
    return mapearEmprestimo(salvo, itens);
  }

  @Override
  @Transactional
  public EmprestimoEventoResponse cancelarEmprestimo(Long id, Long usuarioId) {
    EmprestimoEvento emprestimo = buscarEmprestimo(id);
    emprestimo.setStatus(STATUS_CANCELADO);
    emprestimo.setAtualizadoEm(LocalDateTime.now());
    EmprestimoEvento salvo = emprestimoRepository.salvar(emprestimo);
    List<EmprestimoEventoItem> itens = atualizarStatusItens(salvo.getId(), STATUS_CANCELADO);
    registrarMovimentacao(salvo, "CANCELAMENTO", "Emprestimo cancelado", usuarioId);
    return mapearEmprestimo(salvo, itens);
  }

  @Override
  public List<AgendaResumoDiaResponse> listarAgendaResumo(LocalDate inicio, LocalDate fim) {
    LocalDateTime inicioDateTime = inicio.atStartOfDay();
    LocalDateTime fimDateTime = fim.plusDays(1).atStartOfDay();
    List<EmprestimoEventoAgendaRegistro> registros =
        emprestimoRepository.listarAgendaRegistros(inicioDateTime, fimDateTime);

    Map<LocalDate, List<Long>> emprestimosPorDia = new HashMap<>();
    for (EmprestimoEventoAgendaRegistro registro : registros) {
      LocalDate diaInicio = registro.getRetiradaPrevista().toLocalDate();
      LocalDate diaFim = registro.getDevolucaoPrevista().toLocalDate();
      long dias = ChronoUnit.DAYS.between(diaInicio, diaFim);
      for (int i = 0; i <= dias; i += 1) {
        LocalDate dia = diaInicio.plusDays(i);
        if (dia.isBefore(inicio) || dia.isAfter(fim)) {
          continue;
        }
        emprestimosPorDia.computeIfAbsent(dia, key -> new ArrayList<>()).add(registro.getEmprestimoId());
      }
    }

    List<AgendaResumoDiaResponse> resposta = new ArrayList<>();
    LocalDate cursor = inicio;
    while (!cursor.isAfter(fim)) {
      List<Long> emprestimos = emprestimosPorDia.getOrDefault(cursor, new ArrayList<>());
      AgendaResumoDiaResponse dia = new AgendaResumoDiaResponse();
      dia.setData(cursor);
      dia.setTemBloqueio(!emprestimos.isEmpty());
      dia.setQtdEmprestimos(emprestimos.size());
      dia.setEmprestimoIds(emprestimos);
      resposta.add(dia);
      cursor = cursor.plusDays(1);
    }
    return resposta;
  }

  @Override
  public List<AgendaDiaDetalheResponse> listarAgendaDia(LocalDate data) {
    LocalDateTime inicio = data.atStartOfDay();
    LocalDateTime fim = data.plusDays(1).atStartOfDay();
    List<EmprestimoEventoAgendaRow> rows = emprestimoRepository.listarAgendaDia(inicio, fim);

    Map<Long, AgendaDiaDetalheResponse> agrupado = new HashMap<>();
    for (EmprestimoEventoAgendaRow row : rows) {
      AgendaDiaDetalheResponse resposta = agrupado.get(row.getEmprestimoId());
      if (resposta == null) {
        resposta = new AgendaDiaDetalheResponse();
        resposta.setEmprestimoId(row.getEmprestimoId());
        resposta.setStatus(row.getStatus());
        AgendaDiaDetalheResponse.PeriodoEmprestimoResponse periodo =
            new AgendaDiaDetalheResponse.PeriodoEmprestimoResponse();
        periodo.setRetiradaPrevista(row.getRetiradaPrevista());
        periodo.setDevolucaoPrevista(row.getDevolucaoPrevista());
        periodo.setRetiradaReal(row.getRetiradaReal());
        periodo.setDevolucaoReal(row.getDevolucaoReal());
        resposta.setPeriodo(periodo);
        resposta.setResponsavel(
            row.getResponsavelId() != null
                ? new ResponsavelResumoResponse(row.getResponsavelId(), row.getResponsavelNome())
                : null);
        EventoEmprestimoResponse evento = new EventoEmprestimoResponse();
        evento.setId(row.getEventoId());
        evento.setTitulo(row.getEventoTitulo());
        evento.setLocal(row.getEventoLocal());
        evento.setDataInicio(row.getEventoInicio());
        evento.setDataFim(row.getEventoFim());
        resposta.setEvento(evento);
        resposta.setItens(new ArrayList<>());
        agrupado.put(row.getEmprestimoId(), resposta);
      }

      if (row.getItemId() != null) {
        EmprestimoEventoItemResponse item = new EmprestimoEventoItemResponse();
        item.setItemId(row.getItemId());
        item.setTipoItem(row.getTipoItem());
        item.setQuantidade(row.getQuantidade());
        item.setStatusItem(row.getStatusItem());
        item.setNomeItem(row.getNomeItem());
        item.setNumeroPatrimonio(row.getNumeroPatrimonio());
        resposta.getItens().add(item);
      }
    }

    return agrupado.values().stream()
        .sorted(Comparator.comparing(AgendaDiaDetalheResponse::getEmprestimoId))
        .collect(Collectors.toList());
  }

  @Override
  public DisponibilidadeItemResponse consultarDisponibilidade(
      Long itemId,
      String tipoItem,
      Integer quantidade,
      LocalDateTime inicio,
      LocalDateTime fim,
      Long emprestimoId) {
    List<EmprestimoEventoDisponibilidadeResumo> conflitos =
        emprestimoRepository.buscarConflitosItem(itemId, tipoItem, inicio, fim, emprestimoId);

    DisponibilidadeItemResponse resposta = new DisponibilidadeItemResponse();
    List<ConflitoEmprestimoItemResponse> conflitosResposta =
        conflitos.stream().map(this::mapearConflito).collect(Collectors.toList());
    resposta.setConflitos(conflitosResposta);

    if ("ALMOXARIFADO".equalsIgnoreCase(tipoItem)) {
      Integer estoqueAtual = emprestimoRepository.obterEstoqueAtual(itemId);
      int reservado = conflitos.stream().mapToInt((item) -> item.getQuantidade() != null ? item.getQuantidade() : 0).sum();
      int disponivel = (estoqueAtual != null ? estoqueAtual : 0) - reservado;
      resposta.setQuantidadeDisponivel(disponivel);
      resposta.setDisponivel(disponivel >= (quantidade != null ? quantidade : 0));
    } else {
      resposta.setQuantidadeDisponivel(null);
      resposta.setDisponivel(conflitos.isEmpty());
    }

    return resposta;
  }

  @Override
  public EmprestimoEventoMovimentacaoListaResponse listarMovimentacoes(Long emprestimoId) {
    List<EmprestimoEventoMovimentacaoResponse> respostas = new ArrayList<>();
    for (EmprestimoEventoMovimentacao item : movimentacaoRepository.listarPorEmprestimoId(emprestimoId)) {
      respostas.add(mapper.paraMovimentacaoResposta(item));
    }
    return new EmprestimoEventoMovimentacaoListaResponse(respostas);
  }

  @Override
  @Transactional
  public EventoEmprestimoResponse criarEvento(EventoEmprestimoRequest requisicao) {
    validarEventoRequisicao(requisicao);
    EventoEmprestimo evento = new EventoEmprestimo();
    evento.setTitulo(requisicao.getTitulo());
    evento.setDescricao(requisicao.getDescricao());
    evento.setLocal(requisicao.getLocal());
    evento.setDataInicio(requisicao.getDataInicio());
    evento.setDataFim(requisicao.getDataFim());
    evento.setStatus(requisicao.getStatus() != null ? requisicao.getStatus() : "PLANEJADO");
    LocalDateTime now = LocalDateTime.now();
    evento.setCriadoEm(now);
    evento.setAtualizadoEm(now);
    EventoEmprestimo salvo = eventoRepository.salvar(evento);
    return mapper.paraEventoResposta(salvo);
  }

  @Override
  @Transactional
  public EventoEmprestimoResponse atualizarEvento(Long id, EventoEmprestimoRequest requisicao) {
    validarEventoRequisicao(requisicao);
    EventoEmprestimo evento = buscarEvento(id);
    evento.setTitulo(requisicao.getTitulo());
    evento.setDescricao(requisicao.getDescricao());
    evento.setLocal(requisicao.getLocal());
    evento.setDataInicio(requisicao.getDataInicio());
    evento.setDataFim(requisicao.getDataFim());
    if (requisicao.getStatus() != null && !requisicao.getStatus().trim().isEmpty()) {
      evento.setStatus(requisicao.getStatus());
    }
    evento.setAtualizadoEm(LocalDateTime.now());
    EventoEmprestimo salvo = eventoRepository.salvar(evento);
    return mapper.paraEventoResposta(salvo);
  }

  @Override
  @Transactional
  public void excluirEvento(Long id) {
    EventoEmprestimo evento = buscarEvento(id);
    eventoRepository.remover(evento);
  }

  @Override
  public List<EventoEmprestimoResponse> listarEventos() {
    List<EventoEmprestimoResponse> respostas = new ArrayList<>();
    for (EventoEmprestimo evento : eventoRepository.listar()) {
      respostas.add(mapper.paraEventoResposta(evento));
    }
    return respostas;
  }

  private EmprestimoEvento buscarEmprestimo(Long id) {
    return emprestimoRepository
        .buscarPorId(id)
        .orElseThrow(() -> new IllegalArgumentException("Emprestimo nao encontrado"));
  }

  private EventoEmprestimo buscarEvento(Long id) {
    return eventoRepository
        .buscarPorId(id)
        .orElseThrow(() -> new IllegalArgumentException("Evento nao encontrado"));
  }

  private void validarEmprestimoRequisicao(EmprestimoEventoRequest requisicao) {
    if (requisicao.getEventoId() == null) {
      throw new IllegalArgumentException("Evento e obrigatorio");
    }
    if (requisicao.getDataRetiradaPrevista() == null || requisicao.getDataDevolucaoPrevista() == null) {
      throw new IllegalArgumentException("Periodo do emprestimo e obrigatorio");
    }
    if (requisicao.getDataDevolucaoPrevista().isBefore(requisicao.getDataRetiradaPrevista())) {
      throw new IllegalArgumentException("Data de devolucao deve ser maior que retirada");
    }
    if (requisicao.getStatus() == null || requisicao.getStatus().trim().isEmpty()) {
      requisicao.setStatus(STATUS_RASCUNHO);
    }
  }

  private void validarEventoRequisicao(EventoEmprestimoRequest requisicao) {
    if (requisicao.getTitulo() == null || requisicao.getTitulo().trim().isEmpty()) {
      throw new IllegalArgumentException("Titulo do evento e obrigatorio");
    }
    if (requisicao.getDataInicio() == null || requisicao.getDataFim() == null) {
      throw new IllegalArgumentException("Periodo do evento e obrigatorio");
    }
    if (requisicao.getDataFim().isBefore(requisicao.getDataInicio())) {
      throw new IllegalArgumentException("Data fim deve ser maior que inicio");
    }
  }

  private void aplicarRequisicao(
      EmprestimoEvento emprestimo, EmprestimoEventoRequest requisicao, EventoEmprestimo evento) {
    emprestimo.setEvento(evento);
    emprestimo.setUnidadeId(requisicao.getUnidadeId());
    emprestimo.setResponsavelId(requisicao.getResponsavelId());
    emprestimo.setDataRetiradaPrevista(requisicao.getDataRetiradaPrevista());
    emprestimo.setDataDevolucaoPrevista(requisicao.getDataDevolucaoPrevista());
    emprestimo.setDataRetiradaReal(requisicao.getDataRetiradaReal());
    emprestimo.setDataDevolucaoReal(requisicao.getDataDevolucaoReal());
    emprestimo.setStatus(requisicao.getStatus());
    emprestimo.setObservacoes(requisicao.getObservacoes());
  }

  private void validarDisponibilidadeItens(
      List<EmprestimoEventoItemRequest> itens,
      LocalDateTime inicio,
      LocalDateTime fim,
      Long emprestimoId) {
    if (itens == null || itens.isEmpty()) {
      return;
    }
    for (EmprestimoEventoItemRequest item : itens) {
      if (item.getItemId() == null || item.getTipoItem() == null) {
        continue;
      }
      DisponibilidadeItemResponse disponibilidade =
          consultarDisponibilidade(item.getItemId(), item.getTipoItem(), item.getQuantidade(), inicio, fim, emprestimoId);
      if (!disponibilidade.isDisponivel()) {
        String mensagem = construirMensagemConflito(item, disponibilidade.getConflitos());
        throw new IllegalArgumentException(mensagem);
      }
    }
  }

  private String construirMensagemConflito(
      EmprestimoEventoItemRequest item, List<ConflitoEmprestimoItemResponse> conflitos) {
    if (conflitos == null || conflitos.isEmpty()) {
      return "Item indisponivel para o periodo informado";
    }
    ConflitoEmprestimoItemResponse conflito = conflitos.get(0);
    return "Item indisponivel: evento "
        + conflito.getEventoTitulo()
        + " ("
        + conflito.getInicio()
        + " a "
        + conflito.getFim()
        + ") - status "
        + conflito.getStatus();
  }

  private List<EmprestimoEventoItem> salvarItens(
      EmprestimoEvento emprestimo, List<EmprestimoEventoItemRequest> itens) {
    if (itens == null || itens.isEmpty()) {
      return new ArrayList<>();
    }
    LocalDateTime now = LocalDateTime.now();
    List<EmprestimoEventoItem> entidades = new ArrayList<>();
    for (EmprestimoEventoItemRequest requisicao : itens) {
      EmprestimoEventoItem item = mapper.paraItemEntidade(emprestimo, requisicao);
      item.setStatusItem(
          requisicao.getStatusItem() != null ? requisicao.getStatusItem() : emprestimo.getStatus());
      item.setCriadoEm(now);
      item.setAtualizadoEm(now);
      entidades.add(item);
    }
    return itemRepository.salvarTodos(entidades);
  }

  private List<EmprestimoEventoItem> atualizarStatusItens(Long emprestimoId, String status) {
    List<EmprestimoEventoItem> itens = itemRepository.listarPorEmprestimoId(emprestimoId);
    LocalDateTime now = LocalDateTime.now();
    for (EmprestimoEventoItem item : itens) {
      item.setStatusItem(status);
      item.setAtualizadoEm(now);
    }
    return itemRepository.salvarTodos(itens);
  }

  private EmprestimoEventoResponse mapearEmprestimo(EmprestimoEvento emprestimo) {
    List<EmprestimoEventoItem> itens = itemRepository.listarPorEmprestimoId(emprestimo.getId());
    return mapearEmprestimo(emprestimo, itens);
  }

  private EmprestimoEventoResponse mapearEmprestimo(
      EmprestimoEvento emprestimo, List<EmprestimoEventoItem> itens) {
    List<EmprestimoEventoItemResponse> itensResposta = mapper.paraItensResposta(itens);
    preencherNomesItens(itensResposta);

    String responsavelNome = obterNomeResponsavel(emprestimo.getResponsavelId());
    return mapper.paraEmprestimoResposta(emprestimo, emprestimo.getEvento(), responsavelNome, itensResposta);
  }

  private void preencherNomesItens(List<EmprestimoEventoItemResponse> itens) {
    for (EmprestimoEventoItemResponse item : itens) {
      if (item.getItemId() == null || item.getTipoItem() == null) {
        continue;
      }
      if ("PATRIMONIO".equalsIgnoreCase(item.getTipoItem())) {
        List<Map<String, Object>> dados =
            jdbcTemplate.queryForList(
                "SELECT nome, numero_patrimonio FROM patrimonio_item WHERE id = ?",
                item.getItemId());
        if (!dados.isEmpty()) {
          Map<String, Object> linha = dados.get(0);
          item.setNomeItem(String.valueOf(linha.get("nome")));
          item.setNumeroPatrimonio(String.valueOf(linha.get("numero_patrimonio")));
        }
      } else if ("ALMOXARIFADO".equalsIgnoreCase(item.getTipoItem())) {
        List<Map<String, Object>> dados =
            jdbcTemplate.queryForList(
                "SELECT descricao FROM almoxarifado_item WHERE id = ?",
                item.getItemId());
        if (!dados.isEmpty()) {
          Map<String, Object> linha = dados.get(0);
          item.setNomeItem(String.valueOf(linha.get("descricao")));
        }
      }
    }
  }

  private String obterNomeResponsavel(Long responsavelId) {
    if (responsavelId == null) {
      return null;
    }
    Optional<Usuario> usuario = usuarioRepository.buscarPorId(responsavelId);
    if (!usuario.isPresent()) {
      return null;
    }
    Usuario item = usuario.get();
    return item.getNome() != null && !item.getNome().trim().isEmpty() ? item.getNome() : item.getNomeUsuario();
  }

  private void registrarMovimentacao(
      EmprestimoEvento emprestimo, String acao, String descricao, Long usuarioId) {
    EmprestimoEventoMovimentacao movimentacao = new EmprestimoEventoMovimentacao();
    movimentacao.setEmprestimo(emprestimo);
    movimentacao.setAcao(acao);
    movimentacao.setDescricao(descricao);
    movimentacao.setUsuarioId(usuarioId);
    movimentacao.setCriadoEm(LocalDateTime.now());
    movimentacaoRepository.salvar(movimentacao);
  }

  private ConflitoEmprestimoItemResponse mapearConflito(EmprestimoEventoDisponibilidadeResumo resumo) {
    ConflitoEmprestimoItemResponse resposta = new ConflitoEmprestimoItemResponse();
    resposta.setEmprestimoId(resumo.getEmprestimoId());
    resposta.setEventoTitulo(resumo.getEventoTitulo());
    resposta.setInicio(resumo.getInicio());
    resposta.setFim(resumo.getFim());
    resposta.setStatus(resumo.getStatus());
    resposta.setQuantidadeReservada(resumo.getQuantidade());
    return resposta;
  }
}


