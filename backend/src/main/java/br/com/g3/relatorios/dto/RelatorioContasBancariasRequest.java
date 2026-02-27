package br.com.g3.relatorios.dto;

import java.time.LocalDate;

public class RelatorioContasBancariasRequest {
  private LocalDate dataReferencia;
  private String usuarioEmissor;

  public LocalDate getDataReferencia() {
    return dataReferencia;
  }

  public void setDataReferencia(LocalDate dataReferencia) {
    this.dataReferencia = dataReferencia;
  }

  public String getUsuarioEmissor() {
    return usuarioEmissor;
  }

  public void setUsuarioEmissor(String usuarioEmissor) {
    this.usuarioEmissor = usuarioEmissor;
  }
}
