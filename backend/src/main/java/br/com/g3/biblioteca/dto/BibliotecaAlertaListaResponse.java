package br.com.g3.biblioteca.dto;

import java.util.List;

public class BibliotecaAlertaListaResponse {
  private List<BibliotecaAlertaResponse> alertas;

  public BibliotecaAlertaListaResponse(List<BibliotecaAlertaResponse> alertas) {
    this.alertas = alertas;
  }

  public List<BibliotecaAlertaResponse> getAlertas() {
    return alertas;
  }

  public void setAlertas(List<BibliotecaAlertaResponse> alertas) {
    this.alertas = alertas;
  }
}
