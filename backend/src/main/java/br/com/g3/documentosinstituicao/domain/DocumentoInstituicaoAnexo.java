package br.com.g3.documentosinstituicao.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "documentos_instituicao_anexos")
public class DocumentoInstituicaoAnexo {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "documento_id", nullable = false)
  private Long documentoId;

  @Column(name = "nome_arquivo", nullable = false, length = 200)
  private String nomeArquivo;

  @Column(name = "tipo", nullable = false, length = 30)
  private String tipo;

  @Column(name = "tipo_mime", length = 120)
  private String tipoMime;

  @Column(name = "tamanho", length = 40)
  private String tamanho;

  @Column(name = "caminho_arquivo", columnDefinition = "TEXT")
  private String caminhoArquivo;

  @Column(name = "data_upload", nullable = false)
  private LocalDate dataUpload;

  @Column(name = "usuario", nullable = false, length = 120)
  private String usuario;

  @Column(name = "criado_em", nullable = false)
  private LocalDateTime criadoEm;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getDocumentoId() {
    return documentoId;
  }

  public void setDocumentoId(Long documentoId) {
    this.documentoId = documentoId;
  }

  public String getNomeArquivo() {
    return nomeArquivo;
  }

  public void setNomeArquivo(String nomeArquivo) {
    this.nomeArquivo = nomeArquivo;
  }

  public String getTipo() {
    return tipo;
  }

  public void setTipo(String tipo) {
    this.tipo = tipo;
  }

  public String getTamanho() {
    return tamanho;
  }

  public void setTamanho(String tamanho) {
    this.tamanho = tamanho;
  }

  public String getTipoMime() {
    return tipoMime;
  }

  public void setTipoMime(String tipoMime) {
    this.tipoMime = tipoMime;
  }

  public String getCaminhoArquivo() {
    return caminhoArquivo;
  }

  public void setCaminhoArquivo(String caminhoArquivo) {
    this.caminhoArquivo = caminhoArquivo;
  }

  public LocalDate getDataUpload() {
    return dataUpload;
  }

  public void setDataUpload(LocalDate dataUpload) {
    this.dataUpload = dataUpload;
  }

  public String getUsuario() {
    return usuario;
  }

  public void setUsuario(String usuario) {
    this.usuario = usuario;
  }

  public LocalDateTime getCriadoEm() {
    return criadoEm;
  }

  public void setCriadoEm(LocalDateTime criadoEm) {
    this.criadoEm = criadoEm;
  }
}
