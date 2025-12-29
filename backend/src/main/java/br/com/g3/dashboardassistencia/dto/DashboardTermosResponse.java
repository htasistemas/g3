package br.com.g3.dashboardassistencia.dto;

import java.util.List;

public class DashboardTermosResponse {
  private final long ativos;
  private final double valorTotal;
  private final List<DashboardTermoAlertaResponse> alertas;

  public DashboardTermosResponse(long ativos, double valorTotal, List<DashboardTermoAlertaResponse> alertas) {
    this.ativos = ativos;
    this.valorTotal = valorTotal;
    this.alertas = alertas;
  }

  public long getAtivos() {
    return ativos;
  }

  public double getValorTotal() {
    return valorTotal;
  }

  public List<DashboardTermoAlertaResponse> getAlertas() {
    return alertas;
  }
}
