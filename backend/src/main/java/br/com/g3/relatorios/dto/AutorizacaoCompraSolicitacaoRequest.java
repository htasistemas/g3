package br.com.g3.relatorios.dto;

public class AutorizacaoCompraSolicitacaoRequest {
  private Long solicitacaoId;
  private String usuarioEmissor;

  public Long getSolicitacaoId() {
    return solicitacaoId;
  }

  public void setSolicitacaoId(Long solicitacaoId) {
    this.solicitacaoId = solicitacaoId;
  }

  public String getUsuarioEmissor() {
    return usuarioEmissor;
  }

  public void setUsuarioEmissor(String usuarioEmissor) {
    this.usuarioEmissor = usuarioEmissor;
  }
}
