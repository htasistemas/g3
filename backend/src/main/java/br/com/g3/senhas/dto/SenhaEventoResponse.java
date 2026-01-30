package br.com.g3.senhas.dto;

public class SenhaEventoResponse {
  private String evento;
  private SenhaChamadaResponse dados;

  public SenhaEventoResponse(String evento, SenhaChamadaResponse dados) {
    this.evento = evento;
    this.dados = dados;
  }

  public String getEvento() {
    return evento;
  }

  public SenhaChamadaResponse getDados() {
    return dados;
  }
}
