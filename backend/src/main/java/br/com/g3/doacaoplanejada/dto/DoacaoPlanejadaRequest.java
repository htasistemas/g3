package br.com.g3.doacaoplanejada.dto;

import java.time.LocalDate;

public class DoacaoPlanejadaRequest {
  private Long beneficiarioId;
  private Long vinculoFamiliarId;
  private String itemCodigo;
  private Integer quantidade;
  private LocalDate dataPrevista;
  private String prioridade;
  private String status;
  private String observacoes;
  private String motivoCancelamento;

  public Long getBeneficiarioId() {
    return beneficiarioId;
  }

  public void setBeneficiarioId(Long beneficiarioId) {
    this.beneficiarioId = beneficiarioId;
  }

  public Long getVinculoFamiliarId() {
    return vinculoFamiliarId;
  }

  public void setVinculoFamiliarId(Long vinculoFamiliarId) {
    this.vinculoFamiliarId = vinculoFamiliarId;
  }

  public String getItemCodigo() {
    return itemCodigo;
  }

  public void setItemCodigo(String itemCodigo) {
    this.itemCodigo = itemCodigo;
  }

  public Integer getQuantidade() {
    return quantidade;
  }

  public void setQuantidade(Integer quantidade) {
    this.quantidade = quantidade;
  }

  public LocalDate getDataPrevista() {
    return dataPrevista;
  }

  public void setDataPrevista(LocalDate dataPrevista) {
    this.dataPrevista = dataPrevista;
  }

  public String getPrioridade() {
    return prioridade;
  }

  public void setPrioridade(String prioridade) {
    this.prioridade = prioridade;
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

  public String getMotivoCancelamento() {
    return motivoCancelamento;
  }

  public void setMotivoCancelamento(String motivoCancelamento) {
    this.motivoCancelamento = motivoCancelamento;
  }
}
