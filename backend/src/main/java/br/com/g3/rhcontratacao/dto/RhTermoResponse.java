package br.com.g3.rhcontratacao.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class RhTermoResponse {
  private Long id;
  private Long processoId;
  private String tipo;
  private String dadosJson;
  private String statusAssinatura;
  private LocalDate dataAssinatura;
  private String responsavel;
  private LocalDateTime criadoEm;
  private LocalDateTime atualizadoEm;

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

  public LocalDateTime getCriadoEm() {
    return criadoEm;
  }

  public void setCriadoEm(LocalDateTime criadoEm) {
    this.criadoEm = criadoEm;
  }

  public LocalDateTime getAtualizadoEm() {
    return atualizadoEm;
  }

  public void setAtualizadoEm(LocalDateTime atualizadoEm) {
    this.atualizadoEm = atualizadoEm;
  }
}
