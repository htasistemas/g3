package br.com.g3.cadastrobeneficiario.dto;

import java.util.List;

public class CadastroBeneficiarioResumoListaResponse {
  private List<CadastroBeneficiarioResumoResponse> beneficiarios;

  public CadastroBeneficiarioResumoListaResponse() {}

  public CadastroBeneficiarioResumoListaResponse(List<CadastroBeneficiarioResumoResponse> beneficiarios) {
    this.beneficiarios = beneficiarios;
  }

  public List<CadastroBeneficiarioResumoResponse> getBeneficiarios() {
    return beneficiarios;
  }

  public void setBeneficiarios(List<CadastroBeneficiarioResumoResponse> beneficiarios) {
    this.beneficiarios = beneficiarios;
  }
}
