package br.com.g3.termofomento.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "termo_fomento_documentos")
public class TermoFomentoDocumento {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "termo_fomento_id", nullable = false)
  private Long termoFomentoId;

  @Column(name = "aditivo_id")
  private Long aditivoId;

  @Column(name = "tipo_documento", nullable = false, length = 20)
  private String tipoDocumento;

  @Column(name = "nome", nullable = false, length = 255)
  private String nome;

  @Column(name = "data_url", columnDefinition = "TEXT")
  private String dataUrl;

  @Column(name = "criado_em", nullable = false)
  private LocalDateTime criadoEm;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getTermoFomentoId() {
    return termoFomentoId;
  }

  public void setTermoFomentoId(Long termoFomentoId) {
    this.termoFomentoId = termoFomentoId;
  }

  public Long getAditivoId() {
    return aditivoId;
  }

  public void setAditivoId(Long aditivoId) {
    this.aditivoId = aditivoId;
  }

  public String getTipoDocumento() {
    return tipoDocumento;
  }

  public void setTipoDocumento(String tipoDocumento) {
    this.tipoDocumento = tipoDocumento;
  }

  public String getNome() {
    return nome;
  }

  public void setNome(String nome) {
    this.nome = nome;
  }

  public String getDataUrl() {
    return dataUrl;
  }

  public void setDataUrl(String dataUrl) {
    this.dataUrl = dataUrl;
  }

  public LocalDateTime getCriadoEm() {
    return criadoEm;
  }

  public void setCriadoEm(LocalDateTime criadoEm) {
    this.criadoEm = criadoEm;
  }
}
