package br.com.g3.dashboardassistencia.dto;

public class DashboardTermoAlertaResponse {
  private final String numero;
  private final String vigenciaFim;
  private final String status;

  public DashboardTermoAlertaResponse(String numero, String vigenciaFim, String status) {
    this.numero = numero;
    this.vigenciaFim = vigenciaFim;
    this.status = status;
  }

  public String getNumero() {
    return numero;
  }

  public String getVigenciaFim() {
    return vigenciaFim;
  }

  public String getStatus() {
    return status;
  }
}
