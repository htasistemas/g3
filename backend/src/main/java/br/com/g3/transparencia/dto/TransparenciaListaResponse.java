package br.com.g3.transparencia.dto;

import java.util.List;

public class TransparenciaListaResponse {
  private final List<TransparenciaResponse> transparencias;

  public TransparenciaListaResponse(List<TransparenciaResponse> transparencias) {
    this.transparencias = transparencias;
  }

  public List<TransparenciaResponse> getTransparencias() {
    return transparencias;
  }
}
