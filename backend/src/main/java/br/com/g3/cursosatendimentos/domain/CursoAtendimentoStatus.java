package br.com.g3.cursosatendimentos.domain;

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
@Table(name = "cursos_atendimentos_status")
public class CursoAtendimentoStatus {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "curso_id", nullable = false)
  private CursoAtendimento cursoAtendimento;

  @Column(name = "status", nullable = false, length = 30)
  private String status;

  @Column(name = "data_alteracao", nullable = false)
  private LocalDateTime dataAlteracao;

  @Column(name = "justificativa", columnDefinition = "TEXT")
  private String justificativa;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public CursoAtendimento getCursoAtendimento() {
    return cursoAtendimento;
  }

  public void setCursoAtendimento(CursoAtendimento cursoAtendimento) {
    this.cursoAtendimento = cursoAtendimento;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public LocalDateTime getDataAlteracao() {
    return dataAlteracao;
  }

  public void setDataAlteracao(LocalDateTime dataAlteracao) {
    this.dataAlteracao = dataAlteracao;
  }

  public String getJustificativa() {
    return justificativa;
  }

  public void setJustificativa(String justificativa) {
    this.justificativa = justificativa;
  }
}
