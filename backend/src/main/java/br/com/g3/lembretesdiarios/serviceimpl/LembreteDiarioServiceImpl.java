package br.com.g3.lembretesdiarios.serviceimpl;

import br.com.g3.lembretesdiarios.domain.LembreteDiario;
import br.com.g3.lembretesdiarios.dto.LembreteDiarioAdiarRequest;
import br.com.g3.lembretesdiarios.dto.LembreteDiarioRequest;
import br.com.g3.lembretesdiarios.dto.LembreteDiarioResponse;
import br.com.g3.lembretesdiarios.repository.LembreteDiarioRepository;
import br.com.g3.lembretesdiarios.service.LembreteDiarioService;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class LembreteDiarioServiceImpl implements LembreteDiarioService {
  private static final String RECORRENCIA_DIARIA = "DIARIA";
  private static final String STATUS_PENDENTE = "PENDENTE";
  private static final String STATUS_CONCLUIDO = "CONCLUIDO";
  private static final LocalTime HORA_AVISO_PADRAO = LocalTime.of(9, 0);

  private final LembreteDiarioRepository repository;

  public LembreteDiarioServiceImpl(LembreteDiarioRepository repository) {
    this.repository = repository;
  }

  @Override
  @Transactional(readOnly = true)
  public List<LembreteDiarioResponse> listar(Long usuarioId) {
    List<LembreteDiario> lembretes =
        usuarioId == null ? repository.listarAtivos() : repository.listarAtivosPorUsuario(usuarioId);
    return lembretes.stream().map(this::toResponse).collect(Collectors.toList());
  }

  @Override
  @Transactional
  public LembreteDiarioResponse criar(LembreteDiarioRequest request) {
    LembreteDiario lembrete = new LembreteDiario();
    preencherCamposBasicos(lembrete, request);
    lembrete.setRecorrencia(RECORRENCIA_DIARIA);
    lembrete.setStatus(STATUS_PENDENTE);
    lembrete.setAdiadoAte(null);
    lembrete.setConcluidoEm(null);
    LocalDateTime agora = LocalDateTime.now();
    lembrete.setCriadoEm(agora);
    lembrete.setAtualizadoEm(agora);
    lembrete.setProximaExecucaoEm(calcularProximaExecucao(lembrete));
    return toResponse(repository.salvar(lembrete));
  }

  @Override
  @Transactional
  public LembreteDiarioResponse atualizar(Long id, LembreteDiarioRequest request) {
    LembreteDiario lembrete = buscarAtivo(id);
    preencherCamposBasicos(lembrete, request);
    if (!STATUS_CONCLUIDO.equals(lembrete.getStatus())) {
      lembrete.setProximaExecucaoEm(calcularProximaExecucao(lembrete));
    }
    lembrete.setAtualizadoEm(LocalDateTime.now());
    return toResponse(repository.salvar(lembrete));
  }

  @Override
  @Transactional
  public LembreteDiarioResponse concluir(Long id) {
    LembreteDiario lembrete = buscarAtivo(id);
    lembrete.setStatus(STATUS_CONCLUIDO);
    lembrete.setConcluidoEm(LocalDateTime.now());
    lembrete.setAtualizadoEm(LocalDateTime.now());
    return toResponse(repository.salvar(lembrete));
  }

  @Override
  @Transactional
  public LembreteDiarioResponse adiar(Long id, LembreteDiarioAdiarRequest request) {
    LembreteDiario lembrete = buscarAtivo(id);
    if (STATUS_CONCLUIDO.equals(lembrete.getStatus())) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Lembrete ja concluido");
    }
    LocalDateTime novaData = request.getNovaDataHora();
    if (novaData == null) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Nova data obrigatoria");
    }
    lembrete.setAdiadoAte(novaData);
    lembrete.setProximaExecucaoEm(novaData);
    lembrete.setAtualizadoEm(LocalDateTime.now());
    return toResponse(repository.salvar(lembrete));
  }

  @Override
  @Transactional
  public void excluir(Long id) {
    LembreteDiario lembrete = buscarAtivo(id);
    lembrete.setDeletadoEm(LocalDateTime.now());
    lembrete.setAtualizadoEm(LocalDateTime.now());
    repository.salvar(lembrete);
  }

  private LembreteDiario buscarAtivo(Long id) {
    return repository
        .buscarPorIdAtivo(id)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Lembrete nao encontrado"));
  }

  private void preencherCamposBasicos(LembreteDiario lembrete, LembreteDiarioRequest request) {
    lembrete.setTitulo(request.getTitulo().trim());
    lembrete.setDescricao(request.getDescricao() == null ? null : request.getDescricao().trim());
    lembrete.setDataInicial(request.getDataInicial());
    lembrete.setUsuarioId(request.getUsuarioId());
    lembrete.setTodosUsuarios(Boolean.TRUE.equals(request.getTodosUsuarios()));
    LocalTime hora = request.getHoraAviso() == null ? HORA_AVISO_PADRAO : request.getHoraAviso();
    lembrete.setHoraAviso(hora);
  }

  private LocalDateTime calcularProximaExecucao(LembreteDiario lembrete) {
    if (lembrete.getAdiadoAte() != null) {
      return lembrete.getAdiadoAte();
    }
    LocalDate base = lembrete.getDataInicial();
    LocalDate hoje = LocalDate.now();
    if (base.isBefore(hoje)) {
      base = hoje;
    }
    LocalTime hora = lembrete.getHoraAviso() == null ? HORA_AVISO_PADRAO : lembrete.getHoraAviso();
    return base.atTime(hora);
  }

  private LembreteDiarioResponse toResponse(LembreteDiario lembrete) {
    return new LembreteDiarioResponse(
        lembrete.getId(),
        lembrete.getTitulo(),
        lembrete.getDescricao(),
        lembrete.getDataInicial(),
        lembrete.getUsuarioId(),
        lembrete.isTodosUsuarios(),
        lembrete.getRecorrencia(),
        lembrete.getHoraAviso(),
        lembrete.getStatus(),
        lembrete.getProximaExecucaoEm(),
        lembrete.getAdiadoAte(),
        lembrete.getConcluidoEm(),
        lembrete.getCriadoEm(),
        lembrete.getAtualizadoEm());
  }
}
