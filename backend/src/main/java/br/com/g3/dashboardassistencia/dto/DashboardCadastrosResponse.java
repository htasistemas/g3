package br.com.g3.dashboardassistencia.dto;

public class DashboardCadastrosResponse {
  private final long beneficiarios;
  private final long profissionais;
  private final long voluntarios;
  private final long familias;

  public DashboardCadastrosResponse(
      long beneficiarios, long profissionais, long voluntarios, long familias) {
    this.beneficiarios = beneficiarios;
    this.profissionais = profissionais;
    this.voluntarios = voluntarios;
    this.familias = familias;
  }

  public long getBeneficiarios() {
    return beneficiarios;
  }

  public long getProfissionais() {
    return profissionais;
  }

  public long getVoluntarios() {
    return voluntarios;
  }

  public long getFamilias() {
    return familias;
  }
}
