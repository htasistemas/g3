package br.com.g3.fotoseventos.dto;

import jakarta.validation.constraints.Size;

public class FotoEventoUploadRequest {
  @Size(max = 200)
  private String nomeArquivo;

  @Size(max = 120)
  private String contentType;

  private String conteudo;

  public String getNomeArquivo() {
    return nomeArquivo;
  }

  public void setNomeArquivo(String nomeArquivo) {
    this.nomeArquivo = nomeArquivo;
  }

  public String getContentType() {
    return contentType;
  }

  public void setContentType(String contentType) {
    this.contentType = contentType;
  }

  public String getConteudo() {
    return conteudo;
  }

  public void setConteudo(String conteudo) {
    this.conteudo = conteudo;
  }
}
