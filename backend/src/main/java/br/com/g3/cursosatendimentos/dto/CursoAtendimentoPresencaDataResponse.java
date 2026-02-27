package br.com.g3.cursosatendimentos.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class CursoAtendimentoPresencaDataResponse {
  private Long id;
  private LocalDate dataAula;
  private String status;
  private String observacoes;
  private Integer totalPresencas;
  private Integer totalAnexos;
  private LocalDateTime criadoEm;
  private LocalDateTime atualizadoEm;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public LocalDate getDataAula() {
    return dataAula;
  }

  public void setDataAula(LocalDate dataAula) {
    this.dataAula = dataAula;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getObservacoes() {
    return observacoes;
  }

  public void setObservacoes(String observacoes) {
    this.observacoes = observacoes;
  }

  public Integer getTotalPresencas() {
    return totalPresencas;
  }

  public void setTotalPresencas(Integer totalPresencas) {
    this.totalPresencas = totalPresencas;
  }

  public Integer getTotalAnexos() {
    return totalAnexos;
  }

  public void setTotalAnexos(Integer totalAnexos) {
    this.totalAnexos = totalAnexos;
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
