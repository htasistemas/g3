package br.com.g3.rhcontratacao.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "rh_entrevista")
public class RhEntrevista {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "processo_id", nullable = false)
  private Long processoId;

  @Column(name = "tipo_roteiro", nullable = false, length = 80)
  private String tipoRoteiro;

  @Column(name = "perguntas_json", columnDefinition = "TEXT")
  private String perguntasJson;

  @Column(name = "respostas_json", columnDefinition = "TEXT")
  private String respostasJson;

  @Column(name = "parecer", length = 40)
  private String parecer;

  @Column(name = "observacoes", columnDefinition = "TEXT")
  private String observacoes;

  @Column(name = "data_entrevista")
  private LocalDateTime dataEntrevista;

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

  public String getTipoRoteiro() {
    return tipoRoteiro;
  }

  public void setTipoRoteiro(String tipoRoteiro) {
    this.tipoRoteiro = tipoRoteiro;
  }

  public String getPerguntasJson() {
    return perguntasJson;
  }

  public void setPerguntasJson(String perguntasJson) {
    this.perguntasJson = perguntasJson;
  }

  public String getRespostasJson() {
    return respostasJson;
  }

  public void setRespostasJson(String respostasJson) {
    this.respostasJson = respostasJson;
  }

  public String getParecer() {
    return parecer;
  }

  public void setParecer(String parecer) {
    this.parecer = parecer;
  }

  public String getObservacoes() {
    return observacoes;
  }

  public void setObservacoes(String observacoes) {
    this.observacoes = observacoes;
  }

  public LocalDateTime getDataEntrevista() {
    return dataEntrevista;
  }

  public void setDataEntrevista(LocalDateTime dataEntrevista) {
    this.dataEntrevista = dataEntrevista;
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
