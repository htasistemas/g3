package br.com.g3.chamadotecnico.serviceimpl;

import br.com.g3.auditoria.service.AuditoriaService;
import br.com.g3.chamadotecnico.domain.ChamadoAcaoTipo;
import br.com.g3.chamadotecnico.domain.ChamadoImpacto;
import br.com.g3.chamadotecnico.domain.ChamadoPrioridade;
import br.com.g3.chamadotecnico.domain.ChamadoStatus;
import br.com.g3.chamadotecnico.domain.ChamadoTecnico;
import br.com.g3.chamadotecnico.domain.ChamadoTecnicoAcao;
import br.com.g3.chamadotecnico.domain.ChamadoTecnicoAnexo;
import br.com.g3.chamadotecnico.domain.ChamadoTecnicoAuditoriaVinculo;
import br.com.g3.chamadotecnico.domain.ChamadoTecnicoComentario;
import br.com.g3.chamadotecnico.domain.ChamadoTipo;
import br.com.g3.chamadotecnico.dto.ChamadoTecnicoAcaoResponse;
import br.com.g3.chamadotecnico.dto.ChamadoTecnicoAnexoRequest;
import br.com.g3.chamadotecnico.dto.ChamadoTecnicoAnexoResponse;
import br.com.g3.chamadotecnico.dto.ChamadoTecnicoAtribuicaoRequest;
import br.com.g3.chamadotecnico.dto.ChamadoTecnicoAuditoriaVinculoRequest;
import br.com.g3.chamadotecnico.dto.ChamadoTecnicoAuditoriaVinculoResponse;
import br.com.g3.chamadotecnico.dto.ChamadoTecnicoComentarioRequest;
import br.com.g3.chamadotecnico.dto.ChamadoTecnicoComentarioResponse;
import br.com.g3.chamadotecnico.dto.ChamadoTecnicoCriacaoRequest;
import br.com.g3.chamadotecnico.dto.ChamadoTecnicoAtualizacaoRequest;
import br.com.g3.chamadotecnico.dto.ChamadoTecnicoListaItemResponse;
import br.com.g3.chamadotecnico.dto.ChamadoTecnicoListaResponse;
import br.com.g3.chamadotecnico.dto.ChamadoTecnicoResponse;
import br.com.g3.chamadotecnico.dto.ChamadoTecnicoStatusRequest;
import br.com.g3.chamadotecnico.repository.ChamadoTecnicoAcaoRepository;
import br.com.g3.chamadotecnico.repository.ChamadoTecnicoAnexoRepository;
import br.com.g3.chamadotecnico.repository.ChamadoTecnicoAuditoriaVinculoRepository;
import br.com.g3.chamadotecnico.repository.ChamadoTecnicoComentarioRepository;
import br.com.g3.chamadotecnico.repository.ChamadoTecnicoRepository;
import br.com.g3.chamadotecnico.service.ArmazenamentoChamadoAnexoService;
import br.com.g3.chamadotecnico.service.ChamadoTecnicoService;
import br.com.g3.shared.service.EmailService;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class ChamadoTecnicoServiceImpl implements ChamadoTecnicoService {
  private static final Logger LOGGER = LoggerFactory.getLogger(ChamadoTecnicoServiceImpl.class);
  private static final String DESTINO_PADRAO = "htasistemas@gmail.com";
  private final String destinoChamado;

  private final ChamadoTecnicoRepository chamadoRepository;
  private final ChamadoTecnicoAcaoRepository acaoRepository;
  private final ChamadoTecnicoComentarioRepository comentarioRepository;
  private final ChamadoTecnicoAnexoRepository anexoRepository;
  private final ChamadoTecnicoAuditoriaVinculoRepository vinculoRepository;
  private final ArmazenamentoChamadoAnexoService armazenamentoAnexoService;
  private final AuditoriaService auditoriaService;
  private final EmailService emailService;
  private final JdbcTemplate jdbcTemplate;

  public ChamadoTecnicoServiceImpl(
      ChamadoTecnicoRepository chamadoRepository,
      ChamadoTecnicoAcaoRepository acaoRepository,
      ChamadoTecnicoComentarioRepository comentarioRepository,
      ChamadoTecnicoAnexoRepository anexoRepository,
      ChamadoTecnicoAuditoriaVinculoRepository vinculoRepository,
      ArmazenamentoChamadoAnexoService armazenamentoAnexoService,
      AuditoriaService auditoriaService,
      EmailService emailService,
      JdbcTemplate jdbcTemplate,
      @Value("${app.email.chamados-destino:htasistemas@gmail.com}") String destinoChamado) {
    this.chamadoRepository = chamadoRepository;
    this.acaoRepository = acaoRepository;
    this.comentarioRepository = comentarioRepository;
    this.anexoRepository = anexoRepository;
    this.vinculoRepository = vinculoRepository;
    this.armazenamentoAnexoService = armazenamentoAnexoService;
    this.auditoriaService = auditoriaService;
    this.emailService = emailService;
    this.jdbcTemplate = jdbcTemplate;
    this.destinoChamado = destinoChamado;
  }

  @Override
  @Transactional
  public ChamadoTecnicoResponse criar(ChamadoTecnicoCriacaoRequest request) {
    ChamadoTecnico chamado = new ChamadoTecnico();
    chamado.setId(UUID.randomUUID());
    chamado.setCodigo(gerarCodigoSequencial());
    chamado.setTitulo(request.getTitulo());
    chamado.setDescricao(request.getDescricao());
    chamado.setTipo(request.getTipo());
    chamado.setStatus(
        request.getStatus() != null ? request.getStatus() : ChamadoStatus.ABERTO);
    chamado.setPrioridade(request.getPrioridade());
    chamado.setImpacto(request.getImpacto());
    chamado.setModulo(request.getModulo());
    chamado.setMenu(request.getMenu());
    chamado.setCliente(request.getCliente());
    chamado.setAmbiente(
        request.getAmbiente() == null || request.getAmbiente().trim().isEmpty()
            ? "PRODUCAO"
            : request.getAmbiente().trim());
    chamado.setVersaoSistema(request.getVersaoSistema());
    chamado.setPassosReproducao(request.getPassosReproducao());
    chamado.setResultadoAtual(request.getResultadoAtual());
    chamado.setResultadoEsperado(request.getResultadoEsperado());
    chamado.setUsuariosTeste(request.getUsuariosTeste());
    chamado.setPrazoSlaEmHoras(request.getPrazoSlaEmHoras());
    chamado.setDataLimiteSla(calcularDataLimite(LocalDateTime.now(), request.getPrazoSlaEmHoras()));
    chamado.setResponsavelUsuarioId(request.getResponsavelUsuarioId());
    chamado.setCriadoPorUsuarioId(request.getCriadoPorUsuarioId());
    chamado.setTags(request.getTags());
    LocalDateTime agora = LocalDateTime.now();
    chamado.setCriadoEm(agora);
    chamado.setAtualizadoEm(agora);

    ChamadoTecnico salvo = chamadoRepository.salvar(chamado);
    registrarAcao(
        salvo.getId(),
        ChamadoAcaoTipo.CRIACAO,
        "Chamado criado.",
        null,
        salvo.getStatus(),
        null,
        salvo.getResponsavelUsuarioId(),
        request.getCriadoPorUsuarioId());

    auditoriaService.registrarEvento(
        "Criar chamado tecnico", "chamado_tecnico", salvo.getId().toString(), null, request.getCriadoPorUsuarioId());

    enviarEmailChamado(salvo);
    return mapResponse(salvo);
  }

  @Override
  @Transactional
  public ChamadoTecnicoResponse atualizar(UUID id, ChamadoTecnicoAtualizacaoRequest request) {
    ChamadoTecnico chamado = buscarEntidade(id);
    ChamadoStatus statusAnterior = chamado.getStatus();
    Long responsavelAnterior = chamado.getResponsavelUsuarioId();
    ChamadoPrioridade prioridadeAnterior = chamado.getPrioridade();
    ChamadoImpacto impactoAnterior = chamado.getImpacto();
    ChamadoTipo tipoAnterior = chamado.getTipo();
    String respostaAnterior = chamado.getRespostaDesenvolvedor();

    chamado.setTitulo(request.getTitulo());
    chamado.setDescricao(request.getDescricao());
    chamado.setTipo(request.getTipo());
    chamado.setStatus(request.getStatus());
    chamado.setPrioridade(request.getPrioridade());
    chamado.setImpacto(request.getImpacto());
    chamado.setModulo(request.getModulo());
    chamado.setMenu(request.getMenu());
    chamado.setCliente(request.getCliente());
    chamado.setAmbiente(
        request.getAmbiente() == null || request.getAmbiente().trim().isEmpty()
            ? "PRODUCAO"
            : request.getAmbiente().trim());
    chamado.setVersaoSistema(request.getVersaoSistema());
    chamado.setPassosReproducao(request.getPassosReproducao());
    chamado.setResultadoAtual(request.getResultadoAtual());
    chamado.setResultadoEsperado(request.getResultadoEsperado());
    chamado.setUsuariosTeste(request.getUsuariosTeste());
    chamado.setPrazoSlaEmHoras(request.getPrazoSlaEmHoras());
    chamado.setDataLimiteSla(calcularDataLimite(LocalDateTime.now(), request.getPrazoSlaEmHoras()));
    chamado.setResponsavelUsuarioId(request.getResponsavelUsuarioId());
    if (request.getRespostaDesenvolvedor() != null) {
      chamado.setRespostaDesenvolvedor(request.getRespostaDesenvolvedor());
      if (!request.getRespostaDesenvolvedor().trim().isEmpty()) {
        chamado.setRespondidoEm(LocalDateTime.now());
        chamado.setRespondidoPorUsuarioId(request.getRespondidoPorUsuarioId());
      }
    }
    chamado.setTags(request.getTags());
    chamado.setAtualizadoEm(LocalDateTime.now());

    ChamadoTecnico salvo = chamadoRepository.salvar(chamado);

    StringBuilder descricao = new StringBuilder("Chamado atualizado.");
    if (statusAnterior != chamado.getStatus()) {
      descricao.append(" Status: ").append(statusAnterior).append(" -> ").append(chamado.getStatus()).append(".");
    }
    if (responsavelAnterior != chamado.getResponsavelUsuarioId()) {
      descricao.append(" Responsavel atualizado.");
    }
    if (prioridadeAnterior != chamado.getPrioridade()) {
      descricao.append(" Prioridade atualizada.");
    }
    if (impactoAnterior != chamado.getImpacto()) {
      descricao.append(" Impacto atualizado.");
    }
    if (tipoAnterior != chamado.getTipo()) {
      descricao.append(" Tipo atualizado.");
    }
    if (request.getRespostaDesenvolvedor() != null
        && !request.getRespostaDesenvolvedor().trim().isEmpty()
        && (respostaAnterior == null
            || !respostaAnterior.trim().equals(request.getRespostaDesenvolvedor().trim()))) {
      descricao.append(" Resposta do desenvolvedor registrada.");
    }

    registrarAcao(
        salvo.getId(),
        ChamadoAcaoTipo.EDICAO,
        descricao.toString(),
        statusAnterior,
        chamado.getStatus(),
        responsavelAnterior,
        chamado.getResponsavelUsuarioId(),
        request.getCriadoPorUsuarioId());

    if (request.getRespostaDesenvolvedor() != null
        && !request.getRespostaDesenvolvedor().trim().isEmpty()
        && (respostaAnterior == null
            || !respostaAnterior.trim().equals(request.getRespostaDesenvolvedor().trim()))) {
      registrarAcao(
          salvo.getId(),
          ChamadoAcaoTipo.REGISTRO_ATIVIDADE,
          "Resposta do desenvolvedor registrada.",
          statusAnterior,
          chamado.getStatus(),
          responsavelAnterior,
          chamado.getResponsavelUsuarioId(),
          request.getRespondidoPorUsuarioId());
    }
    if (statusAnterior != chamado.getStatus() && chamado.getStatus() == ChamadoStatus.CANCELADO) {
      registrarAcao(
          salvo.getId(),
          ChamadoAcaoTipo.REGISTRO_ATIVIDADE,
          "Nao sera implementado.",
          statusAnterior,
          chamado.getStatus(),
          responsavelAnterior,
          chamado.getResponsavelUsuarioId(),
          request.getRespondidoPorUsuarioId());
    }

    auditoriaService.registrarEvento(
        "Editar chamado tecnico", "chamado_tecnico", salvo.getId().toString(), null, request.getCriadoPorUsuarioId());
    return mapResponse(salvo);
  }

  @Override
  public ChamadoTecnicoResponse buscarPorId(UUID id) {
    return mapResponse(buscarEntidade(id));
  }

  @Override
  @Transactional
  public void remover(UUID id, Long usuarioId) {
    ChamadoTecnico chamado = buscarEntidade(id);
    registrarAcao(
        id,
        ChamadoAcaoTipo.REGISTRO_ATIVIDADE,
        "Chamado excluido.",
        chamado.getStatus(),
        null,
        chamado.getResponsavelUsuarioId(),
        null,
        usuarioId);
    chamadoRepository.remover(chamado);
    auditoriaService.registrarEvento(
        "Excluir chamado tecnico", "chamado_tecnico", id.toString(), null, usuarioId);
  }

  @Override
  public ChamadoTecnicoListaResponse listar(
      String status,
      String tipo,
      String prioridade,
      String responsavel,
      String modulo,
      String cliente,
      LocalDate dataInicio,
      LocalDate dataFim,
      String texto,
      int pagina,
      int tamanhoPagina) {
    int paginaOk = Math.max(1, pagina);
    int tamanhoOk = Math.max(1, Math.min(tamanhoPagina, 100));
    int offset = (paginaOk - 1) * tamanhoOk;

    String statusNormalizado = normalizarEnum(status, ChamadoStatus.values());
    if (status != null && statusNormalizado == null) {
      return new ChamadoTecnicoListaResponse(new ArrayList<>(), paginaOk, tamanhoOk, 0);
    }
    String tipoNormalizado = normalizarEnum(tipo, ChamadoTipo.values());
    if (tipo != null && tipoNormalizado == null) {
      return new ChamadoTecnicoListaResponse(new ArrayList<>(), paginaOk, tamanhoOk, 0);
    }
    String prioridadeNormalizada = normalizarEnum(prioridade, ChamadoPrioridade.values());
    if (prioridade != null && prioridadeNormalizada == null) {
      return new ChamadoTecnicoListaResponse(new ArrayList<>(), paginaOk, tamanhoOk, 0);
    }

    StringBuilder sql =
        new StringBuilder(
            "SELECT id, codigo, titulo, status, prioridade, responsavel_usuario_id, modulo, cliente, criado_em, data_limite_sla "
                + "FROM chamado_tecnico WHERE 1=1 ");
    StringBuilder countSql = new StringBuilder("SELECT COUNT(*) FROM chamado_tecnico WHERE 1=1 ");
    List<Object> params = new ArrayList<>();

    if (statusNormalizado != null) {
      sql.append(" AND status::text = ? ");
      countSql.append(" AND status::text = ? ");
      params.add(statusNormalizado);
    }
    if (tipoNormalizado != null) {
      sql.append(" AND tipo::text = ? ");
      countSql.append(" AND tipo::text = ? ");
      params.add(tipoNormalizado);
    }
    if (prioridadeNormalizada != null) {
      sql.append(" AND prioridade::text = ? ");
      countSql.append(" AND prioridade::text = ? ");
      params.add(prioridadeNormalizada);
    }
    if (responsavel != null && !responsavel.trim().isEmpty()) {
      sql.append(" AND CAST(responsavel_usuario_id AS TEXT) = ? ");
      countSql.append(" AND CAST(responsavel_usuario_id AS TEXT) = ? ");
      params.add(responsavel.trim());
    }
    if (modulo != null && !modulo.trim().isEmpty()) {
      sql.append(" AND LOWER(unaccent(modulo)) LIKE LOWER(unaccent(?)) ");
      countSql.append(" AND LOWER(unaccent(modulo)) LIKE LOWER(unaccent(?)) ");
      params.add("%" + modulo.trim() + "%");
    }
    if (cliente != null && !cliente.trim().isEmpty()) {
      sql.append(" AND LOWER(unaccent(cliente)) LIKE LOWER(unaccent(?)) ");
      countSql.append(" AND LOWER(unaccent(cliente)) LIKE LOWER(unaccent(?)) ");
      params.add("%" + cliente.trim() + "%");
    }
    if (dataInicio != null) {
      sql.append(" AND criado_em >= ? ");
      countSql.append(" AND criado_em >= ? ");
      params.add(Date.valueOf(dataInicio));
    }
    if (dataFim != null) {
      sql.append(" AND criado_em <= ? ");
      countSql.append(" AND criado_em <= ? ");
      params.add(Date.valueOf(dataFim.plusDays(1)));
    }
    if (texto != null && !texto.trim().isEmpty()) {
      String termo = "%" + texto.trim().toLowerCase() + "%";
      sql.append(
          " AND (LOWER(unaccent(titulo)) LIKE LOWER(unaccent(?)) "
              + "OR LOWER(unaccent(descricao)) LIKE LOWER(unaccent(?)) "
              + "OR LOWER(unaccent(codigo)) LIKE LOWER(unaccent(?))) ");
      countSql.append(
          " AND (LOWER(unaccent(titulo)) LIKE LOWER(unaccent(?)) "
              + "OR LOWER(unaccent(descricao)) LIKE LOWER(unaccent(?)) "
              + "OR LOWER(unaccent(codigo)) LIKE LOWER(unaccent(?))) ");
      params.add(termo);
      params.add(termo);
      params.add(termo);
    }

    sql.append(" ORDER BY criado_em DESC LIMIT ? OFFSET ? ");
    List<Object> listParams = new ArrayList<>(params);
    listParams.add(tamanhoOk);
    listParams.add(offset);

    Long total =
        jdbcTemplate.queryForObject(countSql.toString(), Long.class, params.toArray());
    List<ChamadoTecnicoListaItemResponse> itens =
        jdbcTemplate.query(
            sql.toString(),
            (rs, rowNum) -> {
              ChamadoTecnicoListaItemResponse item = new ChamadoTecnicoListaItemResponse();
              item.setId(UUID.fromString(rs.getString("id")));
              item.setCodigo(rs.getString("codigo"));
              item.setTitulo(rs.getString("titulo"));
              item.setStatus(ChamadoStatus.valueOf(rs.getString("status")));
              item.setPrioridade(ChamadoPrioridade.valueOf(rs.getString("prioridade")));
              Object responsavelId = rs.getObject("responsavel_usuario_id");
              item.setResponsavelUsuarioId(
                  responsavelId == null ? null : ((Number) responsavelId).longValue());
              item.setModulo(rs.getString("modulo"));
              item.setCliente(rs.getString("cliente"));
              item.setCriadoEm(rs.getTimestamp("criado_em").toLocalDateTime());
              if (rs.getTimestamp("data_limite_sla") != null) {
                item.setDataLimiteSla(rs.getTimestamp("data_limite_sla").toLocalDateTime());
              }
              item.setSlaAtrasado(isSlaAtrasado(item.getDataLimiteSla(), item.getStatus()));
              return item;
            },
            listParams.toArray());

    return new ChamadoTecnicoListaResponse(itens, paginaOk, tamanhoOk, total == null ? 0 : total);
  }

  @Override
  @Transactional
  public ChamadoTecnicoResponse alterarStatus(UUID id, ChamadoTecnicoStatusRequest request) {
    ChamadoTecnico chamado = buscarEntidade(id);
    ChamadoStatus statusAnterior = chamado.getStatus();
    chamado.setStatus(request.getStatus());
    chamado.setAtualizadoEm(LocalDateTime.now());
    ChamadoTecnico salvo = chamadoRepository.salvar(chamado);

    registrarAcao(
        salvo.getId(),
        ChamadoAcaoTipo.MUDANCA_STATUS,
        "Status alterado: " + statusAnterior + " -> " + salvo.getStatus(),
        statusAnterior,
        salvo.getStatus(),
        chamado.getResponsavelUsuarioId(),
        chamado.getResponsavelUsuarioId(),
        request.getUsuarioId());

    auditoriaService.registrarEvento(
        "Alterar status chamado tecnico",
        "chamado_tecnico",
        salvo.getId().toString(),
        null,
        request.getUsuarioId());
    return mapResponse(salvo);
  }

  @Override
  @Transactional
  public ChamadoTecnicoResponse atribuirResponsavel(
      UUID id, ChamadoTecnicoAtribuicaoRequest request) {
    ChamadoTecnico chamado = buscarEntidade(id);
    Long anterior = chamado.getResponsavelUsuarioId();
    chamado.setResponsavelUsuarioId(request.getResponsavelUsuarioId());
    chamado.setAtualizadoEm(LocalDateTime.now());
    ChamadoTecnico salvo = chamadoRepository.salvar(chamado);

    registrarAcao(
        salvo.getId(),
        ChamadoAcaoTipo.ATRIBUICAO,
        "Responsavel atualizado.",
        chamado.getStatus(),
        chamado.getStatus(),
        anterior,
        salvo.getResponsavelUsuarioId(),
        request.getUsuarioId());

    auditoriaService.registrarEvento(
        "Atribuir responsavel chamado tecnico",
        "chamado_tecnico",
        salvo.getId().toString(),
        null,
        request.getUsuarioId());
    return mapResponse(salvo);
  }

  @Override
  @Transactional
  public ChamadoTecnicoComentarioResponse adicionarComentario(
      UUID id, ChamadoTecnicoComentarioRequest request) {
    buscarEntidade(id);
    ChamadoTecnicoComentario comentario = new ChamadoTecnicoComentario();
    comentario.setId(UUID.randomUUID());
    comentario.setChamadoId(id);
    comentario.setComentario(request.getComentario());
    comentario.setCriadoPorUsuarioId(request.getUsuarioId());
    comentario.setCriadoEm(LocalDateTime.now());
    ChamadoTecnicoComentario salvo = comentarioRepository.salvar(comentario);

    registrarAcao(
        id,
        ChamadoAcaoTipo.COMENTARIO,
        "Comentario registrado.",
        null,
        null,
        null,
        null,
        request.getUsuarioId());

    auditoriaService.registrarEvento(
        "Comentar chamado tecnico", "chamado_tecnico", id.toString(), null, request.getUsuarioId());
    return mapComentario(salvo);
  }

  @Override
  public List<ChamadoTecnicoComentarioResponse> listarComentarios(UUID id) {
    buscarEntidade(id);
    return comentarioRepository.listarPorChamado(id).stream()
        .map(this::mapComentario)
        .collect(Collectors.toList());
  }

  @Override
  @Transactional
  public ChamadoTecnicoAnexoResponse adicionarAnexo(UUID id, ChamadoTecnicoAnexoRequest request) {
    buscarEntidade(id);
    String path = armazenamentoAnexoService.salvarArquivo(id, request);
    ChamadoTecnicoAnexo anexo = new ChamadoTecnicoAnexo();
    anexo.setId(UUID.randomUUID());
    anexo.setChamadoId(id);
    anexo.setNomeArquivo(request.getNomeArquivo());
    anexo.setMimeType(request.getMimeType());
    anexo.setTamanhoBytes(request.getTamanhoBytes());
    anexo.setStoragePath(path);
    anexo.setCriadoPorUsuarioId(request.getUsuarioId());
    anexo.setCriadoEm(LocalDateTime.now());
    ChamadoTecnicoAnexo salvo = anexoRepository.salvar(anexo);

    registrarAcao(
        id,
        ChamadoAcaoTipo.ANEXO,
        "Anexo adicionado: " + request.getNomeArquivo(),
        null,
        null,
        null,
        null,
        request.getUsuarioId());

    auditoriaService.registrarEvento(
        "Anexar arquivo em chamado tecnico",
        "chamado_tecnico",
        id.toString(),
        null,
        request.getUsuarioId());
    return mapAnexo(salvo);
  }

  @Override
  public List<ChamadoTecnicoAnexoResponse> listarAnexos(UUID id) {
    buscarEntidade(id);
    return anexoRepository.listarPorChamado(id).stream()
        .map(this::mapAnexo)
        .collect(Collectors.toList());
  }

  @Override
  public List<ChamadoTecnicoAcaoResponse> listarAcoes(UUID id) {
    buscarEntidade(id);
    return acaoRepository.listarPorChamado(id).stream()
        .map(this::mapAcao)
        .collect(Collectors.toList());
  }

  @Override
  @Transactional
  public ChamadoTecnicoAuditoriaVinculoResponse vincularAuditoria(
      UUID id, ChamadoTecnicoAuditoriaVinculoRequest request) {
    buscarEntidade(id);
    ChamadoTecnicoAuditoriaVinculo vinculo = new ChamadoTecnicoAuditoriaVinculo();
    vinculo.setId(UUID.randomUUID());
    vinculo.setChamadoId(id);
    vinculo.setAuditoriaEventoId(request.getAuditoriaEventoId());
    vinculo.setCriadoPorUsuarioId(request.getUsuarioId());
    vinculo.setCriadoEm(LocalDateTime.now());
    ChamadoTecnicoAuditoriaVinculo salvo = vinculoRepository.salvar(vinculo);

    registrarAcao(
        id,
        ChamadoAcaoTipo.VINCULO,
        "Auditoria vinculada.",
        null,
        null,
        null,
        null,
        request.getUsuarioId());

    auditoriaService.registrarEvento(
        "Vincular auditoria ao chamado tecnico",
        "chamado_tecnico",
        id.toString(),
        null,
        request.getUsuarioId());

    return mapVinculo(salvo);
  }

  @Override
  public List<ChamadoTecnicoAuditoriaVinculoResponse> listarAuditoriasVinculadas(UUID id) {
    buscarEntidade(id);
    return vinculoRepository.listarPorChamado(id).stream()
        .map(this::mapVinculo)
        .collect(Collectors.toList());
  }

  private ChamadoTecnico buscarEntidade(UUID id) {
    return chamadoRepository
        .buscarPorId(id)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Chamado nao encontrado."));
  }

  private String gerarCodigoSequencial() {
    Integer seq = jdbcTemplate.queryForObject("SELECT nextval('chamado_tecnico_codigo_seq')", Integer.class);
    int valor = seq == null ? 1 : seq;
    return String.format("CT-%06d", valor);
  }

  private LocalDateTime calcularDataLimite(LocalDateTime base, Integer horas) {
    if (horas == null || horas <= 0) {
      return null;
    }
    return base.plusHours(horas);
  }

  private String normalizarEnum(String valor, Enum<?>[] valores) {
    if (valor == null) {
      return null;
    }
    String normalizado = valor.trim().toUpperCase().replace(' ', '_');
    if (normalizado.isEmpty()) {
      return null;
    }
    for (Enum<?> item : valores) {
      if (item.name().equals(normalizado)) {
        return normalizado;
      }
    }
    return null;
  }

  private void registrarAcao(
      UUID chamadoId,
      ChamadoAcaoTipo tipo,
      String descricao,
      ChamadoStatus deStatus,
      ChamadoStatus paraStatus,
      Long deResponsavelId,
      Long paraResponsavelId,
      Long usuarioId) {
    ChamadoTecnicoAcao acao = new ChamadoTecnicoAcao();
    acao.setId(UUID.randomUUID());
    acao.setChamadoId(chamadoId);
    acao.setTipo(tipo);
    acao.setDescricao(descricao);
    acao.setDeStatus(deStatus);
    acao.setParaStatus(paraStatus);
    acao.setDeResponsavelId(deResponsavelId);
    acao.setParaResponsavelId(paraResponsavelId);
    acao.setCriadoPorUsuarioId(usuarioId);
    acao.setCriadoEm(LocalDateTime.now());
    acaoRepository.salvar(acao);
  }

  private ChamadoTecnicoResponse mapResponse(ChamadoTecnico chamado) {
    ChamadoTecnicoResponse response = new ChamadoTecnicoResponse();
    response.setId(chamado.getId());
    response.setCodigo(chamado.getCodigo());
    response.setTitulo(chamado.getTitulo());
    response.setDescricao(chamado.getDescricao());
    response.setTipo(chamado.getTipo());
    response.setStatus(chamado.getStatus());
    response.setPrioridade(chamado.getPrioridade());
    response.setImpacto(chamado.getImpacto());
    response.setModulo(chamado.getModulo());
    response.setMenu(chamado.getMenu());
    response.setCliente(chamado.getCliente());
    response.setAmbiente(chamado.getAmbiente());
    response.setVersaoSistema(chamado.getVersaoSistema());
    response.setPassosReproducao(chamado.getPassosReproducao());
    response.setResultadoAtual(chamado.getResultadoAtual());
    response.setResultadoEsperado(chamado.getResultadoEsperado());
    response.setUsuariosTeste(chamado.getUsuariosTeste());
    response.setPrazoSlaEmHoras(chamado.getPrazoSlaEmHoras());
    response.setDataLimiteSla(chamado.getDataLimiteSla());
    response.setRespostaDesenvolvedor(chamado.getRespostaDesenvolvedor());
    response.setRespondidoEm(chamado.getRespondidoEm());
    response.setRespondidoPorUsuarioId(chamado.getRespondidoPorUsuarioId());
    response.setResponsavelUsuarioId(chamado.getResponsavelUsuarioId());
    response.setCriadoPorUsuarioId(chamado.getCriadoPorUsuarioId());
    response.setTags(chamado.getTags());
    response.setCriadoEm(chamado.getCriadoEm());
    response.setAtualizadoEm(chamado.getAtualizadoEm());
    response.setSlaAtrasado(isSlaAtrasado(chamado.getDataLimiteSla(), chamado.getStatus()));
    return response;
  }

  private ChamadoTecnicoComentarioResponse mapComentario(ChamadoTecnicoComentario comentario) {
    ChamadoTecnicoComentarioResponse response = new ChamadoTecnicoComentarioResponse();
    response.setId(comentario.getId());
    response.setComentario(comentario.getComentario());
    response.setCriadoPorUsuarioId(comentario.getCriadoPorUsuarioId());
    response.setCriadoEm(comentario.getCriadoEm());
    return response;
  }

  private ChamadoTecnicoAnexoResponse mapAnexo(ChamadoTecnicoAnexo anexo) {
    ChamadoTecnicoAnexoResponse response = new ChamadoTecnicoAnexoResponse();
    response.setId(anexo.getId());
    response.setNomeArquivo(anexo.getNomeArquivo());
    response.setMimeType(anexo.getMimeType());
    response.setTamanhoBytes(anexo.getTamanhoBytes());
    response.setStoragePath(anexo.getStoragePath());
    response.setCriadoPorUsuarioId(anexo.getCriadoPorUsuarioId());
    response.setCriadoEm(anexo.getCriadoEm());
    return response;
  }

  private ChamadoTecnicoAcaoResponse mapAcao(ChamadoTecnicoAcao acao) {
    ChamadoTecnicoAcaoResponse response = new ChamadoTecnicoAcaoResponse();
    response.setId(acao.getId());
    response.setTipo(acao.getTipo());
    response.setDescricao(acao.getDescricao());
    response.setDeStatus(acao.getDeStatus());
    response.setParaStatus(acao.getParaStatus());
    response.setDeResponsavelId(acao.getDeResponsavelId());
    response.setParaResponsavelId(acao.getParaResponsavelId());
    response.setCriadoPorUsuarioId(acao.getCriadoPorUsuarioId());
    response.setCriadoEm(acao.getCriadoEm());
    return response;
  }

  private ChamadoTecnicoAuditoriaVinculoResponse mapVinculo(ChamadoTecnicoAuditoriaVinculo vinculo) {
    ChamadoTecnicoAuditoriaVinculoResponse response = new ChamadoTecnicoAuditoriaVinculoResponse();
    response.setId(vinculo.getId());
    response.setCriadoEm(vinculo.getCriadoEm());
    response.setAuditoriaEvento(auditoriaService.buscarPorId(vinculo.getAuditoriaEventoId()));
    return response;
  }

  private boolean isSlaAtrasado(LocalDateTime dataLimite, ChamadoStatus status) {
    if (dataLimite == null) {
      return false;
    }
    if (status == ChamadoStatus.RESOLVIDO || status == ChamadoStatus.FECHADO || status == ChamadoStatus.CANCELADO) {
      return false;
    }
    return LocalDateTime.now().isAfter(dataLimite);
  }

  private void enviarEmailChamado(ChamadoTecnico chamado) {
    ChamadoTecnicoResponse response = mapResponse(chamado);
    try {
      emailService.enviarChamadoTecnico(DESTINO_PADRAO, response);
    } catch (ResponseStatusException ex) {
      LOGGER.warn("Envio de email do chamado ignorado: {}", ex.getReason());
    } catch (Exception ex) {
      LOGGER.warn("Envio de email do chamado falhou e foi ignorado.", ex);
    }
  }
}
