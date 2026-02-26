package br.com.g3.doacaoplanejada.dto;

public class DoacaoPlanejadaConsultaResponse {
  private final DoacaoPlanejadaResponse doacaoPlanejada;

  public DoacaoPlanejadaConsultaResponse(DoacaoPlanejadaResponse doacaoPlanejada) {
    this.doacaoPlanejada = doacaoPlanejada;
  }

  public DoacaoPlanejadaResponse getDoacaoPlanejada() {
    return doacaoPlanejada;
  }
}
