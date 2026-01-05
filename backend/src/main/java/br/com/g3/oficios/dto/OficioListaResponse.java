package br.com.g3.oficios.dto;

import java.util.List;

public class OficioListaResponse {
  private List<OficioResponse> oficios;

  public OficioListaResponse(List<OficioResponse> oficios) {
    this.oficios = oficios;
  }

  public List<OficioResponse> getOficios() {
    return oficios;
  }
}
