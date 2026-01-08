package br.com.g3.cursosatendimentos.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;

public class CursoAtendimentoFilaEsperaResponse {
  private String id;

  @JsonProperty("beneficiaryName")
  private String nomeBeneficiario;

  private String cpf;

  @JsonProperty("joinedAt")
  private LocalDateTime dataEntrada;

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

  public LocalDateTime getDataEntrada() {
    return dataEntrada;
  }

  public void setDataEntrada(LocalDateTime dataEntrada) {
    this.dataEntrada = dataEntrada;
  }
}
