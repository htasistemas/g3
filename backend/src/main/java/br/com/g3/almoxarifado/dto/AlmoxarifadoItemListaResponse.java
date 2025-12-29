package br.com.g3.almoxarifado.dto;

import java.util.List;

public class AlmoxarifadoItemListaResponse {
  private final List<AlmoxarifadoItemResponse> itens;

  public AlmoxarifadoItemListaResponse(List<AlmoxarifadoItemResponse> itens) {
    this.itens = itens;
  }

  public List<AlmoxarifadoItemResponse> getItens() {
    return itens;
  }
}
