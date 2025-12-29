package br.com.g3.tarefaspendencias.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "tarefas_pendencias_checklist")
public class TarefaPendenciaChecklist {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "tarefa_id")
  private TarefaPendencia tarefa;

  @Column(name = "titulo", length = 200, nullable = false)
  private String titulo;

  @Column(name = "concluido", nullable = false)
  private Boolean concluido;

  @Column(name = "concluido_em")
  private LocalDateTime concluidoEm;

  @Column(name = "ordem", nullable = false)
  private Integer ordem;

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

  public TarefaPendencia getTarefa() {
    return tarefa;
  }

  public void setTarefa(TarefaPendencia tarefa) {
    this.tarefa = tarefa;
  }

  public String getTitulo() {
    return titulo;
  }

  public void setTitulo(String titulo) {
    this.titulo = titulo;
  }

  public Boolean getConcluido() {
    return concluido;
  }

  public void setConcluido(Boolean concluido) {
    this.concluido = concluido;
  }

  public LocalDateTime getConcluidoEm() {
    return concluidoEm;
  }

  public void setConcluidoEm(LocalDateTime concluidoEm) {
    this.concluidoEm = concluidoEm;
  }

  public Integer getOrdem() {
    return ordem;
  }

  public void setOrdem(Integer ordem) {
    this.ordem = ordem;
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
