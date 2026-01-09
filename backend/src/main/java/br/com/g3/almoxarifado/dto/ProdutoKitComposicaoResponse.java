package br.com.g3.almoxarifado.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;

public class ProdutoKitComposicaoResponse {
  @JsonProperty("id")
  private final Long id;

  @JsonProperty("produto_item_id")
  private final Long produtoItemId;

  @JsonProperty("produto_item_codigo")
  private final String produtoItemCodigo;

  @JsonProperty("produto_item_descricao")
  private final String produtoItemDescricao;

  @JsonProperty("quantidade_item")
  private final BigDecimal quantidadeItem;

  public ProdutoKitComposicaoResponse(
      Long id,
      Long produtoItemId,
      String produtoItemCodigo,
      String produtoItemDescricao,
      BigDecimal quantidadeItem) {
    this.id = id;
    this.produtoItemId = produtoItemId;
    this.produtoItemCodigo = produtoItemCodigo;
    this.produtoItemDescricao = produtoItemDescricao;
    this.quantidadeItem = quantidadeItem;
  }

  public Long getId() {
    return id;
  }

  public Long getProdutoItemId() {
    return produtoItemId;
  }

  public String getProdutoItemCodigo() {
    return produtoItemCodigo;
  }

  public String getProdutoItemDescricao() {
    return produtoItemDescricao;
  }

  public BigDecimal getQuantidadeItem() {
    return quantidadeItem;
  }
}
