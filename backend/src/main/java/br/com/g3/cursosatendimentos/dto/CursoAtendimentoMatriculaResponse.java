package br.com.g3.cursosatendimentos.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;

public class CursoAtendimentoMatriculaResponse {
  private String id;

  @JsonProperty("beneficiaryName")
  private String nomeBeneficiario;

  private String cpf;

  private String status;

  @JsonProperty("enrolledAt")
  private LocalDateTime dataMatricula;

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
}
