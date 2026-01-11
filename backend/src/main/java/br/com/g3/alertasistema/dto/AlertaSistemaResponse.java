package br.com.g3.alertasistema.dto;

import java.util.List;

public class AlertaSistemaResponse {
  private final List<String> alertasSelecionados;
  private final String frequenciaEnvio;

  public AlertaSistemaResponse(List<String> alertasSelecionados, String frequenciaEnvio) {
    this.alertasSelecionados = alertasSelecionados;
    this.frequenciaEnvio = frequenciaEnvio;
  }

  public List<String> getAlertasSelecionados() {
    return alertasSelecionados;
  }

  public String getFrequenciaEnvio() {
    return frequenciaEnvio;
  }
}
