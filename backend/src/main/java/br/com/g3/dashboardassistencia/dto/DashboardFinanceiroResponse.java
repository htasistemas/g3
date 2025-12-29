package br.com.g3.dashboardassistencia.dto;

public class DashboardFinanceiroResponse {
  private final double valoresAReceber;
  private final double valoresEmCaixa;
  private final double valoresEmBanco;

  public DashboardFinanceiroResponse(double valoresAReceber, double valoresEmCaixa, double valoresEmBanco) {
    this.valoresAReceber = valoresAReceber;
    this.valoresEmCaixa = valoresEmCaixa;
    this.valoresEmBanco = valoresEmBanco;
  }

  public double getValoresAReceber() {
    return valoresAReceber;
  }

  public double getValoresEmCaixa() {
    return valoresEmCaixa;
  }

  public double getValoresEmBanco() {
    return valoresEmBanco;
  }
}
