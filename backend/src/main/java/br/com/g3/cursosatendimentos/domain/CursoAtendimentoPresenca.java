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
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "cursos_atendimentos_presencas")
public class CursoAtendimentoPresenca {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "curso_id", nullable = false)
  private CursoAtendimento cursoAtendimento;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "matricula_id", nullable = false)
  private CursoAtendimentoMatricula matricula;

  @Column(name = "data_aula", nullable = false)
  private LocalDate dataAula;

  @Column(name = "status", nullable = false, length = 10)
  private String status;

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

  public CursoAtendimento getCursoAtendimento() {
    return cursoAtendimento;
  }

  public void setCursoAtendimento(CursoAtendimento cursoAtendimento) {
    this.cursoAtendimento = cursoAtendimento;
  }

  public CursoAtendimentoMatricula getMatricula() {
    return matricula;
  }

  public void setMatricula(CursoAtendimentoMatricula matricula) {
    this.matricula = matricula;
  }

  public LocalDate getDataAula() {
    return dataAula;
  }

  public void setDataAula(LocalDate dataAula) {
    this.dataAula = dataAula;
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
}
