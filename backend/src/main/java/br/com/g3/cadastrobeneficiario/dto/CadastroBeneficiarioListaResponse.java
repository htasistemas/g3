package br.com.g3.cadastrobeneficiario.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class CadastroBeneficiarioListaResponse {
  @JsonProperty("beneficiarios")
  private final List<CadastroBeneficiarioResponse> beneficiarios;

  public CadastroBeneficiarioListaResponse(List<CadastroBeneficiarioResponse> beneficiarios) {
    this.beneficiarios = beneficiarios;
  }

  public List<CadastroBeneficiarioResponse> getBeneficiarios() {
    return beneficiarios;
  }
}
