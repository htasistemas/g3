package br.com.g3.documentosinstituicao.dto;

import java.time.LocalDateTime;

public class DocumentoInstituicaoHistoricoResponse {
  private Long id;
  private Long documentoId;
  private LocalDateTime dataHora;
  private String usuario;
  private String tipoAlteracao;
  private String observacao;
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

  public LocalDateTime getDataHora() {
    return dataHora;
  }

  public void setDataHora(LocalDateTime dataHora) {
    this.dataHora = dataHora;
  }

  public String getUsuario() {
    return usuario;
  }

  public void setUsuario(String usuario) {
    this.usuario = usuario;
  }

  public String getTipoAlteracao() {
    return tipoAlteracao;
  }

  public void setTipoAlteracao(String tipoAlteracao) {
    this.tipoAlteracao = tipoAlteracao;
  }

  public String getObservacao() {
    return observacao;
  }

  public void setObservacao(String observacao) {
    this.observacao = observacao;
  }

  public LocalDateTime getCriadoEm() {
    return criadoEm;
  }

  public void setCriadoEm(LocalDateTime criadoEm) {
    this.criadoEm = criadoEm;
  }
}
