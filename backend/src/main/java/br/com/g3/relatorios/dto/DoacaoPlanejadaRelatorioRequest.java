package br.com.g3.relatorios.dto;

public class DoacaoPlanejadaRelatorioRequest {
  private String usuarioEmissor;

  public DoacaoPlanejadaRelatorioRequest() {}

  public DoacaoPlanejadaRelatorioRequest(String usuarioEmissor) {
    this.usuarioEmissor = usuarioEmissor;
  }

  public String getUsuarioEmissor() {
    return usuarioEmissor;
  }

  public void setUsuarioEmissor(String usuarioEmissor) {
    this.usuarioEmissor = usuarioEmissor;
  }
}
