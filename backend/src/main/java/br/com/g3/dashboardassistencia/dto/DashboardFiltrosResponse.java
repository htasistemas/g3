package br.com.g3.dashboardassistencia.dto;

public class DashboardFiltrosResponse {
  private final String startDate;
  private final String endDate;

  public DashboardFiltrosResponse(String startDate, String endDate) {
    this.startDate = startDate;
    this.endDate = endDate;
  }

  public String getStartDate() {
    return startDate;
  }

  public String getEndDate() {
    return endDate;
  }
}
