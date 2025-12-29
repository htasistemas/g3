package br.com.g3.almoxarifado.dto;

import java.util.List;

public class AlmoxarifadoMovimentacaoListaResponse {
  private final List<AlmoxarifadoMovimentacaoResponse> movimentacoes;

  public AlmoxarifadoMovimentacaoListaResponse(
      List<AlmoxarifadoMovimentacaoResponse> movimentacoes) {
    this.movimentacoes = movimentacoes;
  }

  public List<AlmoxarifadoMovimentacaoResponse> getMovimentacoes() {
    return movimentacoes;
  }
}
