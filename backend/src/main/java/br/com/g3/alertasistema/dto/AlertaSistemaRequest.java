package br.com.g3.alertasistema.dto;

import java.util.List;

public class AlertaSistemaRequest {
  private List<String> alertasSelecionados;
  private String frequenciaEnvio;

  public List<String> getAlertasSelecionados() {
    return alertasSelecionados;
  }

  public void setAlertasSelecionados(List<String> alertasSelecionados) {
    this.alertasSelecionados = alertasSelecionados;
  }

  public String getFrequenciaEnvio() {
    return frequenciaEnvio;
  }

  public void setFrequenciaEnvio(String frequenciaEnvio) {
    this.frequenciaEnvio = frequenciaEnvio;
  }
}
