package br.com.g3.almoxarifado.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public class ProdutoKitComposicaoRequest {
  @NotNull
  @JsonProperty("produto_item_id")
  private Long produtoItemId;

  @NotNull
  @JsonProperty("quantidade_item")
  private BigDecimal quantidadeItem;

  public Long getProdutoItemId() {
    return produtoItemId;
  }

  public void setProdutoItemId(Long produtoItemId) {
    this.produtoItemId = produtoItemId;
  }

  public BigDecimal getQuantidadeItem() {
    return quantidadeItem;
  }

  public void setQuantidadeItem(BigDecimal quantidadeItem) {
    this.quantidadeItem = quantidadeItem;
  }
}
