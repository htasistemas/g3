package br.com.g3.rhcontratacao.dto;

import java.time.LocalDate;

public class RhTermoRequest {
  private String tipo;
  private String dadosJson;
  private String statusAssinatura;
  private LocalDate dataAssinatura;
  private String responsavel;

  public String getTipo() {
    return tipo;
  }

  public void setTipo(String tipo) {
    this.tipo = tipo;
  }

  public String getDadosJson() {
    return dadosJson;
  }

  public void setDadosJson(String dadosJson) {
    this.dadosJson = dadosJson;
  }

  public String getStatusAssinatura() {
    return statusAssinatura;
  }

  public void setStatusAssinatura(String statusAssinatura) {
    this.statusAssinatura = statusAssinatura;
  }

  public LocalDate getDataAssinatura() {
    return dataAssinatura;
  }

  public void setDataAssinatura(LocalDate dataAssinatura) {
    this.dataAssinatura = dataAssinatura;
  }

  public String getResponsavel() {
    return responsavel;
  }

  public void setResponsavel(String responsavel) {
    this.responsavel = responsavel;
  }
}
