package br.com.g3.chamadotecnico.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "chamado_tecnico_anexo")
public class ChamadoTecnicoAnexo {
  @Id
  @Column(name = "id", columnDefinition = "uuid")
  private UUID id;

  @Column(name = "chamado_id", columnDefinition = "uuid", nullable = false)
  private UUID chamadoId;

  @Column(name = "nome_arquivo", length = 255)
  private String nomeArquivo;

  @Column(name = "mime_type", length = 120)
  private String mimeType;

  @Column(name = "tamanho_bytes")
  private Long tamanhoBytes;

  @Column(name = "storage_path", length = 600)
  private String storagePath;

  @Column(name = "hash_sha256", length = 120)
  private String hashSha256;

  @Column(name = "criado_por_usuario_id")
  private Long criadoPorUsuarioId;

  @Column(name = "criado_em", nullable = false)
  private LocalDateTime criadoEm;

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public UUID getChamadoId() {
    return chamadoId;
  }

  public void setChamadoId(UUID chamadoId) {
    this.chamadoId = chamadoId;
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

  public String getHashSha256() {
    return hashSha256;
  }

  public void setHashSha256(String hashSha256) {
    this.hashSha256 = hashSha256;
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
