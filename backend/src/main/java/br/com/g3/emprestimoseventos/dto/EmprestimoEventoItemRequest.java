package br.com.g3.emprestimoseventos.dto;

public class EmprestimoEventoItemRequest {
  private Long itemId;
  private String tipoItem;
  private Integer quantidade;
  private String statusItem;
  private String observacaoItem;

  public Long getItemId() {
    return itemId;
  }

  public void setItemId(Long itemId) {
    this.itemId = itemId;
  }

  public String getTipoItem() {
    return tipoItem;
  }

  public void setTipoItem(String tipoItem) {
    this.tipoItem = tipoItem;
  }

  public Integer getQuantidade() {
    return quantidade;
  }

  public void setQuantidade(Integer quantidade) {
    this.quantidade = quantidade;
  }

  public String getStatusItem() {
    return statusItem;
  }

  public void setStatusItem(String statusItem) {
    this.statusItem = statusItem;
  }

  public String getObservacaoItem() {
    return observacaoItem;
  }

  public void setObservacaoItem(String observacaoItem) {
    this.observacaoItem = observacaoItem;
  }
}
