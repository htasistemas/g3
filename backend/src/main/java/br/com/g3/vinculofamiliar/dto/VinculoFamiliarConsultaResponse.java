package br.com.g3.vinculofamiliar.dto;

public class VinculoFamiliarConsultaResponse {
  private final VinculoFamiliarResponse familia;

  public VinculoFamiliarConsultaResponse(VinculoFamiliarResponse familia) {
    this.familia = familia;
  }

  public VinculoFamiliarResponse getFamilia() {
    return familia;
  }
}
