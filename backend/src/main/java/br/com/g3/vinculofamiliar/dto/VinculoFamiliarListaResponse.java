package br.com.g3.vinculofamiliar.dto;

import java.util.List;

public class VinculoFamiliarListaResponse {
  private final List<VinculoFamiliarResponse> familias;

  public VinculoFamiliarListaResponse(List<VinculoFamiliarResponse> familias) {
    this.familias = familias;
  }

  public List<VinculoFamiliarResponse> getFamilias() {
    return familias;
  }
}
