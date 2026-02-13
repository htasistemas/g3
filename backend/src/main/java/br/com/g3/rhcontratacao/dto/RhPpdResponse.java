package br.com.g3.rhcontratacao.dto;

import java.time.LocalDateTime;

public class RhPpdResponse {
  private Long id;
  private Long processoId;
  private String cabecalhoJson;
  private String ladoAJson;
  private String ladoBJson;
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

  public String getCabecalhoJson() {
    return cabecalhoJson;
  }

  public void setCabecalhoJson(String cabecalhoJson) {
    this.cabecalhoJson = cabecalhoJson;
  }

  public String getLadoAJson() {
    return ladoAJson;
  }

  public void setLadoAJson(String ladoAJson) {
    this.ladoAJson = ladoAJson;
  }

  public String getLadoBJson() {
    return ladoBJson;
  }

  public void setLadoBJson(String ladoBJson) {
    this.ladoBJson = ladoBJson;
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
