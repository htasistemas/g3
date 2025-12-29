package br.com.g3.doacaorealizada.dto;

public class DoacaoRealizadaConsultaResponse {
  private final DoacaoRealizadaResponse doacao;

  public DoacaoRealizadaConsultaResponse(DoacaoRealizadaResponse doacao) {
    this.doacao = doacao;
  }

  public DoacaoRealizadaResponse getDoacao() {
    return doacao;
  }
}
