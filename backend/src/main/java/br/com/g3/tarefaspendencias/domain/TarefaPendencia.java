package br.com.g3.tarefaspendencias.domain;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tarefas_pendencias")
public class TarefaPendencia {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "titulo", length = 200, nullable = false)
  private String titulo;

  @Column(name = "descricao", nullable = false)
  private String descricao;

  @Column(name = "responsavel", length = 150, nullable = false)
  private String responsavel;

  @Column(name = "prioridade", length = 20, nullable = false)
  private String prioridade;

  @Column(name = "prazo")
  private LocalDate prazo;

  @Column(name = "status", length = 40, nullable = false)
  private String status;

  @OneToMany(
      mappedBy = "tarefa",
      cascade = CascadeType.ALL,
      fetch = FetchType.EAGER,
      orphanRemoval = true)
  private List<TarefaPendenciaChecklist> checklist = new ArrayList<>();

  @OneToMany(
      mappedBy = "tarefa",
      cascade = CascadeType.ALL,
      fetch = FetchType.EAGER,
      orphanRemoval = true)
  private List<TarefaPendenciaHistorico> historico = new ArrayList<>();

  @Column(name = "criado_em", nullable = false)
  private LocalDateTime criadoEm;

  @Column(name = "atualizado_em", nullable = false)
  private LocalDateTime atualizadoEm;

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

  public List<TarefaPendenciaChecklist> getChecklist() {
    return checklist;
  }

  public void setChecklist(List<TarefaPendenciaChecklist> checklist) {
    this.checklist = checklist;
  }

  public List<TarefaPendenciaHistorico> getHistorico() {
    return historico;
  }

  public void setHistorico(List<TarefaPendenciaHistorico> historico) {
    this.historico = historico;
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
}
