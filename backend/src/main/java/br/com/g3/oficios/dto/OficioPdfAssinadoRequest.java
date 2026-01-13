package br.com.g3.oficios.dto;

public class OficioPdfAssinadoRequest {
  private String nomeArquivo;
  private String tipoMime;
  private String conteudoBase64;

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

  public String getConteudoBase64() {
    return conteudoBase64;
  }

  public void setConteudoBase64(String conteudoBase64) {
    this.conteudoBase64 = conteudoBase64;
  }
}
