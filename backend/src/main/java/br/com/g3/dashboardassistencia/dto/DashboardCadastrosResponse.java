package br.com.g3.dashboardassistencia.dto;

public class DashboardCadastrosResponse {
  private final long beneficiarios;
  private final long profissionais;
  private final long voluntarios;
  private final long familias;
  private final long bensPatrimonio;

  public DashboardCadastrosResponse(
      long beneficiarios, long profissionais, long voluntarios, long familias, long bensPatrimonio) {
    this.beneficiarios = beneficiarios;
    this.profissionais = profissionais;
    this.voluntarios = voluntarios;
    this.familias = familias;
    this.bensPatrimonio = bensPatrimonio;
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

  public long getBensPatrimonio() {
    return bensPatrimonio;
  }
}
