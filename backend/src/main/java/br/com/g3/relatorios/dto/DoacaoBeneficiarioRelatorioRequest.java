package br.com.g3.relatorios.dto;

public class DoacaoBeneficiarioRelatorioRequest {
  private Long beneficiarioId;
  private String usuarioEmissor;

  public DoacaoBeneficiarioRelatorioRequest() {}

  public DoacaoBeneficiarioRelatorioRequest(Long beneficiarioId, String usuarioEmissor) {
    this.beneficiarioId = beneficiarioId;
    this.usuarioEmissor = usuarioEmissor;
  }

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
