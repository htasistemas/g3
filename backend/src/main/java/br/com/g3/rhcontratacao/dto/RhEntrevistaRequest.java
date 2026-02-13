package br.com.g3.rhcontratacao.dto;

import java.time.LocalDateTime;

public class RhEntrevistaRequest {
  private Long processoId;
  private String tipoRoteiro;
  private String perguntasJson;
  private String respostasJson;
  private String parecer;
  private String observacoes;
  private LocalDateTime dataEntrevista;

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
}
