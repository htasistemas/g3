package br.com.g3.prontuario.dto;

import jakarta.validation.constraints.NotBlank;

public class ProntuarioAnexoRequest {
  @NotBlank
  private String nomeArquivo;

  private String tipoMime;

  @NotBlank
  private String urlArquivo;

  public String getNomeArquivo() {
    return nomeArquivo;
  }

  public void setNomeArquivo(String nomeArquivo) {
    this.nomeArquivo = nomeArquivo;
  }

  public String getTipoMime() {
    return tipoMime;
  }

  public void setTipoMime(String tipoMime) {
    this.tipoMime = tipoMime;
  }

  public String getUrlArquivo() {
    return urlArquivo;
  }

  public void setUrlArquivo(String urlArquivo) {
    this.urlArquivo = urlArquivo;
  }
}
