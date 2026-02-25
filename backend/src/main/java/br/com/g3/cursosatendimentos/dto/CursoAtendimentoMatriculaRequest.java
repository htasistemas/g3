package br.com.g3.cursosatendimentos.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class CursoAtendimentoMatriculaRequest {
  private String id;

  @JsonProperty("beneficiaryName")
  private String nomeBeneficiario;

  private String cpf;

  private String status;

  @JsonProperty("enrolledAt")
  private LocalDateTime dataMatricula;

  @JsonProperty("dataAgendada")
  private LocalDate dataAgendada;

  @JsonProperty("horaAgendada")
  private String horaAgendada;

  @JsonProperty("statusAgendamento")
  private String statusAgendamento;

  @JsonProperty("profissionalId")
  private String profissionalId;

  @JsonProperty("profissionalNome")
  private String profissionalNome;

  @JsonProperty("profissionalTipo")
  private String profissionalTipo;

  @JsonProperty("confirmacaoPresenca")
  private Boolean confirmacaoPresenca;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getNomeBeneficiario() {
    return nomeBeneficiario;
  }

  public void setNomeBeneficiario(String nomeBeneficiario) {
    this.nomeBeneficiario = nomeBeneficiario;
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

  public String getHoraAgendada() {
    return horaAgendada;
  }

  public void setHoraAgendada(String horaAgendada) {
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
