package br.com.g3.tarefaspendencias.serviceimpl;

import br.com.g3.tarefaspendencias.domain.TarefaPendencia;
import br.com.g3.tarefaspendencias.domain.TarefaPendenciaChecklist;
import br.com.g3.tarefaspendencias.domain.TarefaPendenciaHistorico;
import br.com.g3.tarefaspendencias.dto.TarefaPendenciaChecklistRequest;
import br.com.g3.tarefaspendencias.dto.TarefaPendenciaRequest;
import br.com.g3.tarefaspendencias.dto.TarefaPendenciaResponse;
import br.com.g3.tarefaspendencias.mapper.TarefaPendenciaMapper;
import br.com.g3.tarefaspendencias.repository.TarefaPendenciaRepository;
import br.com.g3.tarefaspendencias.service.TarefaPendenciaService;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
public class TarefaPendenciaServiceImpl implements TarefaPendenciaService {
  private final TarefaPendenciaRepository repository;

  public TarefaPendenciaServiceImpl(TarefaPendenciaRepository repository) {
    this.repository = repository;
  }

  @Override
  public List<TarefaPendenciaResponse> listar() {
    return repository.listar().stream().map(TarefaPendenciaMapper::toResponse).collect(Collectors.toList());
  }

  @Override
  public TarefaPendenciaResponse buscarPorId(Long id) {
    TarefaPendencia tarefa =
        repository.buscarPorId(id).orElseThrow(() -> new IllegalArgumentException("Tarefa nao encontrada."));
    return TarefaPendenciaMapper.toResponse(tarefa);
  }

  @Override
  @Transactional
  public TarefaPendenciaResponse criar(TarefaPendenciaRequest request) {
    TarefaPendencia tarefa = TarefaPendenciaMapper.toDomain(request);
    LocalDateTime agora = LocalDateTime.now();
    tarefa.setCriadoEm(agora);
    tarefa.setAtualizadoEm(agora);
    tarefa.setStatus(resolveStatus(request.getStatus()));
    tarefa.getChecklist().clear();
    tarefa.getChecklist().addAll(construirChecklist(request.getChecklist(), tarefa, agora));
    registrarHistorico(tarefa, "Tarefa criada com status " + tarefa.getStatus(), agora);
    return TarefaPendenciaMapper.toResponse(repository.salvar(tarefa));
  }

  @Override
  @Transactional
  public TarefaPendenciaResponse atualizar(Long id, TarefaPendenciaRequest request) {
    TarefaPendencia tarefa =
        repository.buscarPorId(id).orElseThrow(() -> new IllegalArgumentException("Tarefa nao encontrada."));
    LocalDateTime agora = LocalDateTime.now();
    tarefa.setTitulo(request.getTitulo());
    tarefa.setDescricao(request.getDescricao());
    tarefa.setResponsavel(request.getResponsavel());
    tarefa.setPrioridade(request.getPrioridade());
    tarefa.setPrazo(request.getPrazo());
    tarefa.setStatus(resolveStatus(request.getStatus()));
    tarefa.getChecklist().clear();
    tarefa.getChecklist().addAll(construirChecklist(request.getChecklist(), tarefa, agora));
    registrarHistorico(tarefa, "Tarefa atualizada com status " + tarefa.getStatus(), agora);
    tarefa.setAtualizadoEm(agora);
    return TarefaPendenciaMapper.toResponse(repository.salvar(tarefa));
  }

  @Override
  @Transactional
  public void remover(Long id) {
    TarefaPendencia tarefa =
        repository.buscarPorId(id).orElseThrow(() -> new IllegalArgumentException("Tarefa nao encontrada."));
    repository.remover(tarefa);
  }

  private String resolveStatus(String status) {
    return StringUtils.hasText(status) ? status : "Aberta";
  }

  private List<TarefaPendenciaChecklist> construirChecklist(
      List<TarefaPendenciaChecklistRequest> itens, TarefaPendencia tarefa, LocalDateTime agora) {
    List<TarefaPendenciaChecklist> resultado = new ArrayList<>();
    if (itens == null) {
      return resultado;
    }
    int ordem = 0;
    for (TarefaPendenciaChecklistRequest item : itens) {
      TarefaPendenciaChecklist registro = new TarefaPendenciaChecklist();
      registro.setId(item.getId());
      registro.setTarefa(tarefa);
      registro.setTitulo(item.getTitulo());
      boolean concluido = Boolean.TRUE.equals(item.getConcluido());
      registro.setConcluido(concluido);
      registro.setOrdem(item.getOrdem() != null ? item.getOrdem() : ordem);
      if (concluido) {
        registro.setConcluidoEm(item.getConcluidoEm() != null ? item.getConcluidoEm() : agora);
      } else {
        registro.setConcluidoEm(null);
      }
      registro.setCriadoEm(agora);
      registro.setAtualizadoEm(agora);
      resultado.add(registro);
      ordem++;
    }
    return resultado;
  }

  private TarefaPendenciaHistorico criarHistorico(TarefaPendencia tarefa, String mensagem, LocalDateTime agora) {
    TarefaPendenciaHistorico historico = new TarefaPendenciaHistorico();
    historico.setTarefa(tarefa);
    historico.setMensagem(mensagem);
    historico.setCriadoEm(agora);
    return historico;
  }

  private void registrarHistorico(TarefaPendencia tarefa, String mensagem, LocalDateTime agora) {
    List<TarefaPendenciaHistorico> historico = tarefa.getHistorico();
    if (historico == null) {
      historico = new ArrayList<>();
      tarefa.setHistorico(historico);
    }
    historico.add(criarHistorico(tarefa, mensagem, agora));
  }
}
