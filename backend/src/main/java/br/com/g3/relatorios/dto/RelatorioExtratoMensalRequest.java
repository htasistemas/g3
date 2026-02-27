package br.com.g3.relatorios.dto;

public class RelatorioExtratoMensalRequest {
  private String mesReferencia;
  private String usuarioEmissor;

  public String getMesReferencia() {
    return mesReferencia;
  }

  public void setMesReferencia(String mesReferencia) {
    this.mesReferencia = mesReferencia;
  }

  public String getUsuarioEmissor() {
    return usuarioEmissor;
  }

  public void setUsuarioEmissor(String usuarioEmissor) {
    this.usuarioEmissor = usuarioEmissor;
  }
}
