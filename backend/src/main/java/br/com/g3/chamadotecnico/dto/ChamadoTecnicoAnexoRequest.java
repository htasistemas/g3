package br.com.g3.chamadotecnico.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;

public class ChamadoTecnicoAnexoRequest {
  @JsonProperty("nome_arquivo")
  @NotBlank
  private String nomeArquivo;

  @JsonProperty("mime_type")
  @NotBlank
  private String mimeType;

  @JsonProperty("tamanho_bytes")
  private Long tamanhoBytes;

  @JsonProperty("conteudo_base64")
  @NotBlank
  private String conteudoBase64;

  @JsonProperty("usuario_id")
  private Long usuarioId;

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

  public Long getUsuarioId() {
    return usuarioId;
  }

  public void setUsuarioId(Long usuarioId) {
    this.usuarioId = usuarioId;
  }
}
