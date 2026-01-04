package br.com.g3.planotrabalho.dto;

import java.time.LocalDate;

public class PlanoTrabalhoEtapaRequest {
  private Long id;
  private String descricao;
  private String status;
  private LocalDate dataInicioPrevista;
  private LocalDate dataFimPrevista;
  private LocalDate dataConclusao;
  private String responsavel;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getDescricao() {
    return descricao;
  }

  public void setDescricao(String descricao) {
    this.descricao = descricao;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public LocalDate getDataInicioPrevista() {
    return dataInicioPrevista;
  }

  public void setDataInicioPrevista(LocalDate dataInicioPrevista) {
    this.dataInicioPrevista = dataInicioPrevista;
  }

  public LocalDate getDataFimPrevista() {
    return dataFimPrevista;
  }

  public void setDataFimPrevista(LocalDate dataFimPrevista) {
    this.dataFimPrevista = dataFimPrevista;
  }

  public LocalDate getDataConclusao() {
    return dataConclusao;
  }

  public void setDataConclusao(LocalDate dataConclusao) {
    this.dataConclusao = dataConclusao;
  }

  public String getResponsavel() {
    return responsavel;
  }

  public void setResponsavel(String responsavel) {
    this.responsavel = responsavel;
  }
}
