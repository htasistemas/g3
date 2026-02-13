package br.com.g3.rhcontratacao.dto;

public class RhArquivoRequest {
  private String categoria;
  private String tipoDocumento;
  private String nomeArquivo;
  private String mimeType;
  private Long tamanhoBytes;
  private String conteudoBase64;

  public String getCategoria() {
    return categoria;
  }

  public void setCategoria(String categoria) {
    this.categoria = categoria;
  }

  public String getTipoDocumento() {
    return tipoDocumento;
  }

  public void setTipoDocumento(String tipoDocumento) {
    this.tipoDocumento = tipoDocumento;
  }

  public String getNomeArquivo() {
    return nomeArquivo;
  }

  public void setNomeArquivo(String nomeArquivo) {
    this.nomeArquivo = nomeArquivo;
  }

  public String getMimeType() {
    return mimeType;
  }

  public void setMimeType(String mimeType) {
    this.mimeType = mimeType;
  }

  public Long getTamanhoBytes() {
    return tamanhoBytes;
  }

  public void setTamanhoBytes(Long tamanhoBytes) {
    this.tamanhoBytes = tamanhoBytes;
  }

  public String getConteudoBase64() {
    return conteudoBase64;
  }

  public void setConteudoBase64(String conteudoBase64) {
    this.conteudoBase64 = conteudoBase64;
  }
}
