package br.com.g3.cadastrobeneficiario.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CadastroBeneficiarioConsultaResponse {
  @JsonProperty("beneficiario")
  private final CadastroBeneficiarioResponse beneficiario;

  public CadastroBeneficiarioConsultaResponse(CadastroBeneficiarioResponse beneficiario) {
    this.beneficiario = beneficiario;
  }

  public CadastroBeneficiarioResponse getBeneficiario() {
    return beneficiario;
  }
}
