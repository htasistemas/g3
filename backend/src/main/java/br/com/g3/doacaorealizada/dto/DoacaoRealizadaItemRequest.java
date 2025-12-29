package br.com.g3.doacaorealizada.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class DoacaoRealizadaItemRequest {
  @NotNull
  private Long itemId;

  @NotNull
  @Min(1)
  private Integer quantidade;

  private String observacoes;

  public Long getItemId() {
    return itemId;
  }

  public void setItemId(Long itemId) {
    this.itemId = itemId;
  }

  public Integer getQuantidade() {
    return quantidade;
  }

  public void setQuantidade(Integer quantidade) {
    this.quantidade = quantidade;
  }

  public String getObservacoes() {
    return observacoes;
  }

  public void setObservacoes(String observacoes) {
    this.observacoes = observacoes;
  }
}
