package br.com.g3.documentosinstituicao.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class DocumentoInstituicaoAnexoResponse {
  private Long id;
  private Long documentoId;
  private String nomeArquivo;
  private String tipo;
  private String tipoMime;
  private String tamanho;
  private String arquivoUrl;
  private LocalDate dataUpload;
  private String usuario;
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

  public String getArquivoUrl() {
    return arquivoUrl;
  }

  public void setArquivoUrl(String arquivoUrl) {
    this.arquivoUrl = arquivoUrl;
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
