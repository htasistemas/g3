package br.com.g3.planotrabalho.dto;

import java.time.LocalDate;

public class PlanoTrabalhoEtapaResponse {
  private final Long id;
  private final String descricao;
  private final String status;
  private final LocalDate dataInicioPrevista;
  private final LocalDate dataFimPrevista;
  private final LocalDate dataConclusao;
  private final String responsavel;

  public PlanoTrabalhoEtapaResponse(
      Long id,
      String descricao,
      String status,
      LocalDate dataInicioPrevista,
      LocalDate dataFimPrevista,
      LocalDate dataConclusao,
      String responsavel) {
    this.id = id;
    this.descricao = descricao;
    this.status = status;
    this.dataInicioPrevista = dataInicioPrevista;
    this.dataFimPrevista = dataFimPrevista;
    this.dataConclusao = dataConclusao;
    this.responsavel = responsavel;
  }

  public Long getId() {
    return id;
  }

  public String getDescricao() {
    return descricao;
  }

  public String getStatus() {
    return status;
  }

  public LocalDate getDataInicioPrevista() {
    return dataInicioPrevista;
  }

  public LocalDate getDataFimPrevista() {
    return dataFimPrevista;
  }

  public LocalDate getDataConclusao() {
    return dataConclusao;
  }

  public String getResponsavel() {
    return responsavel;
  }
}
