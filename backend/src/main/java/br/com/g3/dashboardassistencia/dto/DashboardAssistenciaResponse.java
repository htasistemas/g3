package br.com.g3.dashboardassistencia.dto;

public class DashboardAssistenciaResponse {
  private final DashboardFiltrosResponse filters;
  private final DashboardCadastrosResponse cadastros;
  private final DashboardTop12Response top12;
  private final DashboardAtendimentoResponse atendimento;
  private final DashboardFamiliasResponse familias;
  private final DashboardTermosResponse termos;
  private final DashboardFinanceiroResponse financeiro;

  public DashboardAssistenciaResponse(
      DashboardFiltrosResponse filters,
      DashboardCadastrosResponse cadastros,
      DashboardTop12Response top12,
      DashboardAtendimentoResponse atendimento,
      DashboardFamiliasResponse familias,
      DashboardTermosResponse termos,
      DashboardFinanceiroResponse financeiro) {
    this.filters = filters;
    this.cadastros = cadastros;
    this.top12 = top12;
    this.atendimento = atendimento;
    this.familias = familias;
    this.termos = termos;
    this.financeiro = financeiro;
  }

  public DashboardFiltrosResponse getFilters() {
    return filters;
  }

  public DashboardCadastrosResponse getCadastros() {
    return cadastros;
  }

  public DashboardTop12Response getTop12() {
    return top12;
  }

  public DashboardAtendimentoResponse getAtendimento() {
    return atendimento;
  }

  public DashboardFamiliasResponse getFamilias() {
    return familias;
  }

  public DashboardTermosResponse getTermos() {
    return termos;
  }

  public DashboardFinanceiroResponse getFinanceiro() {
    return financeiro;
  }
}
