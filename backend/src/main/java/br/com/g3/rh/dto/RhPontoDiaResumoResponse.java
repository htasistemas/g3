package br.com.g3.rh.dto;

import java.time.LocalDate;

public class RhPontoDiaResumoResponse {
  private LocalDate data;
  private String ocorrencia;
  private String entradaManha;
  private String saidaManha;
  private String entradaTarde;
  private String saidaTarde;
  private Integer totalTrabalhadoMinutos;
  private Integer extrasMinutos;
  private Integer faltasAtrasosMinutos;
  private String observacoes;
  private Long pontoDiaId;

  public LocalDate getData() {
    return data;
  }

  public void setData(LocalDate data) {
    this.data = data;
  }

  public String getOcorrencia() {
    return ocorrencia;
  }

  public void setOcorrencia(String ocorrencia) {
    this.ocorrencia = ocorrencia;
  }

  public String getEntradaManha() {
    return entradaManha;
  }

  public void setEntradaManha(String entradaManha) {
    this.entradaManha = entradaManha;
  }

  public String getSaidaManha() {
    return saidaManha;
  }

  public void setSaidaManha(String saidaManha) {
    this.saidaManha = saidaManha;
  }

  public String getEntradaTarde() {
    return entradaTarde;
  }

  public void setEntradaTarde(String entradaTarde) {
    this.entradaTarde = entradaTarde;
  }

  public String getSaidaTarde() {
    return saidaTarde;
  }

  public void setSaidaTarde(String saidaTarde) {
    this.saidaTarde = saidaTarde;
  }

  public Integer getTotalTrabalhadoMinutos() {
    return totalTrabalhadoMinutos;
  }

  public void setTotalTrabalhadoMinutos(Integer totalTrabalhadoMinutos) {
    this.totalTrabalhadoMinutos = totalTrabalhadoMinutos;
  }

  public Integer getExtrasMinutos() {
    return extrasMinutos;
  }

  public void setExtrasMinutos(Integer extrasMinutos) {
    this.extrasMinutos = extrasMinutos;
  }

  public Integer getFaltasAtrasosMinutos() {
    return faltasAtrasosMinutos;
  }

  public void setFaltasAtrasosMinutos(Integer faltasAtrasosMinutos) {
    this.faltasAtrasosMinutos = faltasAtrasosMinutos;
  }

  public String getObservacoes() {
    return observacoes;
  }

  public void setObservacoes(String observacoes) {
    this.observacoes = observacoes;
  }

  public Long getPontoDiaId() {
    return pontoDiaId;
  }

  public void setPontoDiaId(Long pontoDiaId) {
    this.pontoDiaId = pontoDiaId;
  }
}
