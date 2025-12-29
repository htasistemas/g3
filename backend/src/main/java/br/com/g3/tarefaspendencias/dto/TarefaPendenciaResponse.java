package br.com.g3.tarefaspendencias.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TarefaPendenciaResponse {
  private Long id;
  private String titulo;
  private String descricao;
  private String responsavel;
  private String prioridade;
  private LocalDate prazo;
  private String status;
  private LocalDateTime criadoEm;
  private LocalDateTime atualizadoEm;
  private List<TarefaPendenciaChecklistResponse> checklist = new ArrayList<>();
  private List<TarefaPendenciaHistoricoResponse> historico = new ArrayList<>();

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getTitulo() {
    return titulo;
  }

  public void setTitulo(String titulo) {
    this.titulo = titulo;
  }

  public String getDescricao() {
    return descricao;
  }

  public void setDescricao(String descricao) {
    this.descricao = descricao;
  }

  public String getResponsavel() {
    return responsavel;
  }

  public void setResponsavel(String responsavel) {
    this.responsavel = responsavel;
  }

  public String getPrioridade() {
    return prioridade;
  }

  public void setPrioridade(String prioridade) {
    this.prioridade = prioridade;
  }

  public LocalDate getPrazo() {
    return prazo;
  }

  public void setPrazo(LocalDate prazo) {
    this.prazo = prazo;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public LocalDateTime getCriadoEm() {
    return criadoEm;
  }

  public void setCriadoEm(LocalDateTime criadoEm) {
    this.criadoEm = criadoEm;
  }

  public LocalDateTime getAtualizadoEm() {
    return atualizadoEm;
  }

  public void setAtualizadoEm(LocalDateTime atualizadoEm) {
    this.atualizadoEm = atualizadoEm;
  }

  public List<TarefaPendenciaChecklistResponse> getChecklist() {
    return checklist;
  }

  public void setChecklist(List<TarefaPendenciaChecklistResponse> checklist) {
    this.checklist = checklist;
  }

  public List<TarefaPendenciaHistoricoResponse> getHistorico() {
    return historico;
  }

  public void setHistorico(List<TarefaPendenciaHistoricoResponse> historico) {
    this.historico = historico;
  }
}
