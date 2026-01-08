package br.com.g3.relatorios.dto;

public class BeneficiarioFichaRequest {
  private Long beneficiarioId;
  private String usuarioEmissor;

  public Long getBeneficiarioId() {
    return beneficiarioId;
  }

  public void setBeneficiarioId(Long beneficiarioId) {
    this.beneficiarioId = beneficiarioId;
  }

  public String getUsuarioEmissor() {
    return usuarioEmissor;
  }

  public void setUsuarioEmissor(String usuarioEmissor) {
    this.usuarioEmissor = usuarioEmissor;
  }
}
