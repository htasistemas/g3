package br.com.g3.doacaoplanejada.dto;

import java.time.LocalDate;

public class DoacaoPlanejadaResponse {
  private final Long id;
  private final Long beneficiarioId;
  private final Long vinculoFamiliarId;
  private final String itemCodigo;
  private final String itemDescricao;
  private final String itemUnidade;
  private final Integer quantidade;
  private final LocalDate dataPrevista;
  private final String prioridade;
  private final String status;
  private final String observacoes;
  private final String motivoCancelamento;

  public DoacaoPlanejadaResponse(
      Long id,
      Long beneficiarioId,
      Long vinculoFamiliarId,
      String itemCodigo,
      String itemDescricao,
      String itemUnidade,
      Integer quantidade,
      LocalDate dataPrevista,
      String prioridade,
      String status,
      String observacoes,
      String motivoCancelamento) {
    this.id = id;
    this.beneficiarioId = beneficiarioId;
    this.vinculoFamiliarId = vinculoFamiliarId;
    this.itemCodigo = itemCodigo;
    this.itemDescricao = itemDescricao;
    this.itemUnidade = itemUnidade;
    this.quantidade = quantidade;
    this.dataPrevista = dataPrevista;
    this.prioridade = prioridade;
    this.status = status;
    this.observacoes = observacoes;
    this.motivoCancelamento = motivoCancelamento;
  }

  public Long getId() {
    return id;
  }

  public Long getBeneficiarioId() {
    return beneficiarioId;
  }

  public Long getVinculoFamiliarId() {
    return vinculoFamiliarId;
  }

  public String getItemCodigo() {
    return itemCodigo;
  }

  public String getItemDescricao() {
    return itemDescricao;
  }

  public String getItemUnidade() {
    return itemUnidade;
  }

  public Integer getQuantidade() {
    return quantidade;
  }

  public LocalDate getDataPrevista() {
    return dataPrevista;
  }

  public String getPrioridade() {
    return prioridade;
  }

  public String getStatus() {
    return status;
  }

  public String getObservacoes() {
    return observacoes;
  }

  public String getMotivoCancelamento() {
    return motivoCancelamento;
  }
}
