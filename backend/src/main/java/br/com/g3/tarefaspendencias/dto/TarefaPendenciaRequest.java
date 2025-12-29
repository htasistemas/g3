package br.com.g3.tarefaspendencias.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TarefaPendenciaRequest {
  @NotBlank
  private String titulo;

  @NotBlank
  private String descricao;

  @NotBlank
  private String responsavel;

  @NotBlank
  private String prioridade;

  private LocalDate prazo;

  @NotBlank
  private String status;

  @Valid
  private List<TarefaPendenciaChecklistRequest> checklist = new ArrayList<>();

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

  public List<TarefaPendenciaChecklistRequest> getChecklist() {
    return checklist;
  }

  public void setChecklist(List<TarefaPendenciaChecklistRequest> checklist) {
    this.checklist = checklist;
  }
}
