package br.com.g3.doacaorealizada.dto;

import java.util.List;

public class DoacaoRealizadaListaResponse {
  private final List<DoacaoRealizadaResponse> doacoes;

  public DoacaoRealizadaListaResponse(List<DoacaoRealizadaResponse> doacoes) {
    this.doacoes = doacoes;
  }

  public List<DoacaoRealizadaResponse> getDoacoes() {
    return doacoes;
  }
}
