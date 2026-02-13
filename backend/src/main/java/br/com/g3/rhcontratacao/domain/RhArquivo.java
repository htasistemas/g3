package br.com.g3.rhcontratacao.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "rh_arquivo")
public class RhArquivo {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "processo_id", nullable = false)
  private Long processoId;

  @Column(name = "categoria", nullable = false, length = 80)
  private String categoria;

  @Column(name = "tipo_documento", length = 160)
  private String tipoDocumento;

  @Column(name = "nome_arquivo", nullable = false, length = 255)
  private String nomeArquivo;

  @Column(name = "mime_type", nullable = false, length = 120)
  private String mimeType;

  @Column(name = "tamanho_bytes", nullable = false)
  private Long tamanhoBytes;

  @Column(name = "caminho_arquivo", nullable = false, columnDefinition = "TEXT")
  private String caminhoArquivo;

  @Column(name = "versao", nullable = false)
  private Integer versao;

  @Column(name = "criado_por")
  private Long criadoPor;

  @Column(name = "criado_em", nullable = false)
  private LocalDateTime criadoEm;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getProcessoId() {
    return processoId;
  }

  public void setProcessoId(Long processoId) {
    this.processoId = processoId;
  }

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

  public String getCaminhoArquivo() {
    return caminhoArquivo;
  }

  public void setCaminhoArquivo(String caminhoArquivo) {
    this.caminhoArquivo = caminhoArquivo;
  }

  public Integer getVersao() {
    return versao;
  }

  public void setVersao(Integer versao) {
    this.versao = versao;
  }

  public Long getCriadoPor() {
    return criadoPor;
  }

  public void setCriadoPor(Long criadoPor) {
    this.criadoPor = criadoPor;
  }

  public LocalDateTime getCriadoEm() {
    return criadoEm;
  }

  public void setCriadoEm(LocalDateTime criadoEm) {
    this.criadoEm = criadoEm;
  }
}
