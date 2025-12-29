package br.com.g3.doacaorealizada.dto;

public class DoacaoRealizadaItemResponse {
  private final Long id;
  private final Long itemId;
  private final String codigoItem;
  private final String descricaoItem;
  private final String unidadeItem;
  private final Integer quantidade;
  private final String observacoes;

  public DoacaoRealizadaItemResponse(
      Long id,
      Long itemId,
      String codigoItem,
      String descricaoItem,
      String unidadeItem,
      Integer quantidade,
      String observacoes) {
    this.id = id;
    this.itemId = itemId;
    this.codigoItem = codigoItem;
    this.descricaoItem = descricaoItem;
    this.unidadeItem = unidadeItem;
    this.quantidade = quantidade;
    this.observacoes = observacoes;
  }

  public Long getId() {
    return id;
  }

  public Long getItemId() {
    return itemId;
  }

  public String getCodigoItem() {
    return codigoItem;
  }

  public String getDescricaoItem() {
    return descricaoItem;
  }

  public String getUnidadeItem() {
    return unidadeItem;
  }

  public Integer getQuantidade() {
    return quantidade;
  }

  public String getObservacoes() {
    return observacoes;
  }
}
