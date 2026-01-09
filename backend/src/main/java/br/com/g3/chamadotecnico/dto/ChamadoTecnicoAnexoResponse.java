package br.com.g3.chamadotecnico.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;
import java.util.UUID;

public class ChamadoTecnicoAnexoResponse {
  private UUID id;
  @JsonProperty("nome_arquivo")
  private String nomeArquivo;
  @JsonProperty("mime_type")
  private String mimeType;
  @JsonProperty("tamanho_bytes")
  private Long tamanhoBytes;
  @JsonProperty("storage_path")
  private String storagePath;
  @JsonProperty("criado_por_usuario_id")
  private Long criadoPorUsuarioId;
  @JsonProperty("criado_em")
  private LocalDateTime criadoEm;

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
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

  public String getStoragePath() {
    return storagePath;
  }

  public void setStoragePath(String storagePath) {
    this.storagePath = storagePath;
  }

  public Long getCriadoPorUsuarioId() {
    return criadoPorUsuarioId;
  }

  public void setCriadoPorUsuarioId(Long criadoPorUsuarioId) {
    this.criadoPorUsuarioId = criadoPorUsuarioId;
  }

  public LocalDateTime getCriadoEm() {
    return criadoEm;
  }

  public void setCriadoEm(LocalDateTime criadoEm) {
    this.criadoEm = criadoEm;
  }
}
