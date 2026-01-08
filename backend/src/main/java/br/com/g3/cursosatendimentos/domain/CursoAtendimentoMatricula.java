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
@Table(name = "cursos_atendimentos_matriculas")
public class CursoAtendimentoMatricula {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "curso_id", nullable = false)
  private CursoAtendimento cursoAtendimento;

  @Column(name = "beneficiario_nome", nullable = false, length = 200)
  private String beneficiarioNome;

  @Column(name = "cpf", length = 20)
  private String cpf;

  @Column(name = "status", nullable = false, length = 20)
  private String status;

  @Column(name = "data_matricula", nullable = false)
  private LocalDateTime dataMatricula;

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

  public String getBeneficiarioNome() {
    return beneficiarioNome;
  }

  public void setBeneficiarioNome(String beneficiarioNome) {
    this.beneficiarioNome = beneficiarioNome;
  }

  public String getCpf() {
    return cpf;
  }

  public void setCpf(String cpf) {
    this.cpf = cpf;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public LocalDateTime getDataMatricula() {
    return dataMatricula;
  }

  public void setDataMatricula(LocalDateTime dataMatricula) {
    this.dataMatricula = dataMatricula;
  }
}
