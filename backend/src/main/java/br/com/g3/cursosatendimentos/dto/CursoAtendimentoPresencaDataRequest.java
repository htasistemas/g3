package br.com.g3.cursosatendimentos.dto;

import java.time.LocalDate;

public class CursoAtendimentoPresencaDataRequest {
  private LocalDate dataAula;
  private String observacoes;
  private String status;

  public LocalDate getDataAula() {
    return dataAula;
  }

  public void setDataAula(LocalDate dataAula) {
    this.dataAula = dataAula;
  }

  public String getObservacoes() {
    return observacoes;
  }

  public void setObservacoes(String observacoes) {
    this.observacoes = observacoes;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }
}
