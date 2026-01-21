package br.com.g3.chamadas.beneficiario.dto;

public class BeneficiarioRespostaDto {
  private Long idBeneficiario;
  private String nomeBeneficiario;

  public BeneficiarioRespostaDto(Long idBeneficiario, String nomeBeneficiario) {
    this.idBeneficiario = idBeneficiario;
    this.nomeBeneficiario = nomeBeneficiario;
  }

  public Long getIdBeneficiario() {
    return idBeneficiario;
  }

  public String getNomeBeneficiario() {
    return nomeBeneficiario;
  }
}
