package br.com.g3.tarefaspendencias.mapper;

import br.com.g3.tarefaspendencias.domain.TarefaPendencia;
import br.com.g3.tarefaspendencias.domain.TarefaPendenciaChecklist;
import br.com.g3.tarefaspendencias.domain.TarefaPendenciaHistorico;
import br.com.g3.tarefaspendencias.dto.TarefaPendenciaChecklistResponse;
import br.com.g3.tarefaspendencias.dto.TarefaPendenciaHistoricoResponse;
import br.com.g3.tarefaspendencias.dto.TarefaPendenciaRequest;
import br.com.g3.tarefaspendencias.dto.TarefaPendenciaResponse;
import java.util.List;
import java.util.stream.Collectors;

public final class TarefaPendenciaMapper {
  private TarefaPendenciaMapper() {}

  public static TarefaPendencia toDomain(TarefaPendenciaRequest request) {
    TarefaPendencia tarefa = new TarefaPendencia();
    tarefa.setTitulo(request.getTitulo());
    tarefa.setDescricao(request.getDescricao());
    tarefa.setResponsavel(request.getResponsavel());
    tarefa.setPrioridade(request.getPrioridade());
    tarefa.setPrazo(request.getPrazo());
    tarefa.setStatus(request.getStatus());
    return tarefa;
  }

  public static TarefaPendenciaResponse toResponse(TarefaPendencia domain) {
    TarefaPendenciaResponse response = new TarefaPendenciaResponse();
    response.setId(domain.getId());
    response.setTitulo(domain.getTitulo());
    response.setDescricao(domain.getDescricao());
    response.setResponsavel(domain.getResponsavel());
    response.setPrioridade(domain.getPrioridade());
    response.setPrazo(domain.getPrazo());
    response.setStatus(domain.getStatus());
    response.setCriadoEm(domain.getCriadoEm());
    response.setAtualizadoEm(domain.getAtualizadoEm());
    response.setChecklist(domain.getChecklist().stream().map(TarefaPendenciaMapper::toChecklistResponse).collect(Collectors.toList()));
    response.setHistorico(domain.getHistorico().stream().map(TarefaPendenciaMapper::toHistoricoResponse).collect(Collectors.toList()));
    return response;
  }

  private static TarefaPendenciaChecklistResponse toChecklistResponse(TarefaPendenciaChecklist checklist) {
    TarefaPendenciaChecklistResponse response = new TarefaPendenciaChecklistResponse();
    response.setId(checklist.getId());
    response.setTitulo(checklist.getTitulo());
    response.setConcluido(checklist.getConcluido());
    response.setConcluidoEm(checklist.getConcluidoEm());
    response.setOrdem(checklist.getOrdem());
    return response;
  }

  private static TarefaPendenciaHistoricoResponse toHistoricoResponse(TarefaPendenciaHistorico historico) {
    TarefaPendenciaHistoricoResponse response = new TarefaPendenciaHistoricoResponse();
    response.setId(historico.getId());
    response.setMensagem(historico.getMensagem());
    response.setCriadoEm(historico.getCriadoEm());
    return response;
  }
}
