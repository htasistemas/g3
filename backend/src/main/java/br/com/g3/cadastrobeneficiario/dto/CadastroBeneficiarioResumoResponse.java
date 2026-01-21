package br.com.g3.cadastrobeneficiario.dto;

public class CadastroBeneficiarioResumoResponse {
  private Long idBeneficiario;
  private String nomeBeneficiario;

  public CadastroBeneficiarioResumoResponse() {}

  public CadastroBeneficiarioResumoResponse(Long idBeneficiario, String nomeBeneficiario) {
    this.idBeneficiario = idBeneficiario;
    this.nomeBeneficiario = nomeBeneficiario;
  }

  public Long getIdBeneficiario() {
    return idBeneficiario;
  }

  public void setIdBeneficiario(Long idBeneficiario) {
    this.idBeneficiario = idBeneficiario;
  }

  public String getNomeBeneficiario() {
    return nomeBeneficiario;
  }

  public void setNomeBeneficiario(String nomeBeneficiario) {
    this.nomeBeneficiario = nomeBeneficiario;
  }
}
