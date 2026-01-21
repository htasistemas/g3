package br.com.g3.documentosinstituicao.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "documentos_instituicao_historico")
public class DocumentoInstituicaoHistorico {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "documento_id", nullable = false)
  private Long documentoId;

  @Column(name = "data_hora", nullable = false)
  private LocalDateTime dataHora;

  @Column(name = "usuario", nullable = false, length = 120)
  private String usuario;

  @Column(name = "tipo_alteracao", nullable = false, length = 60)
  private String tipoAlteracao;

  @Column(name = "observacao", columnDefinition = "TEXT")
  private String observacao;

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
