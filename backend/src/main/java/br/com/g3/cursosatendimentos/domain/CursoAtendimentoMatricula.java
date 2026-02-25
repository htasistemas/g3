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
import java.time.LocalTime;

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

  @Column(name = "data_agendada")
  private LocalDate dataAgendada;

  @Column(name = "hora_agendada")
  private LocalTime horaAgendada;

  @Column(name = "status_agendamento", length = 30)
  private String statusAgendamento;

  @Column(name = "profissional_id", length = 40)
  private String profissionalId;

  @Column(name = "profissional_nome", length = 200)
  private String profissionalNome;

  @Column(name = "profissional_tipo", length = 20)
  private String profissionalTipo;

  @Column(name = "confirmacao_presenca", nullable = false)
  private Boolean confirmacaoPresenca = false;

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

  public LocalDate getDataAgendada() {
    return dataAgendada;
  }

  public void setDataAgendada(LocalDate dataAgendada) {
    this.dataAgendada = dataAgendada;
  }

  public LocalTime getHoraAgendada() {
    return horaAgendada;
  }

  public void setHoraAgendada(LocalTime horaAgendada) {
    this.horaAgendada = horaAgendada;
  }

  public String getStatusAgendamento() {
    return statusAgendamento;
  }

  public void setStatusAgendamento(String statusAgendamento) {
    this.statusAgendamento = statusAgendamento;
  }

  public String getProfissionalId() {
    return profissionalId;
  }

  public void setProfissionalId(String profissionalId) {
    this.profissionalId = profissionalId;
  }

  public String getProfissionalNome() {
    return profissionalNome;
  }

  public void setProfissionalNome(String profissionalNome) {
    this.profissionalNome = profissionalNome;
  }

  public String getProfissionalTipo() {
    return profissionalTipo;
  }

  public void setProfissionalTipo(String profissionalTipo) {
    this.profissionalTipo = profissionalTipo;
  }

  public Boolean getConfirmacaoPresenca() {
    return confirmacaoPresenca;
  }

  public void setConfirmacaoPresenca(Boolean confirmacaoPresenca) {
    this.confirmacaoPresenca = confirmacaoPresenca;
  }
}
