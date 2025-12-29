package br.com.g3.almoxarifado.dto;

public class AlmoxarifadoMovimentacaoCadastroResponse {
  private final AlmoxarifadoMovimentacaoResponse movimentacao;
  private final AlmoxarifadoItemResponse item;

  public AlmoxarifadoMovimentacaoCadastroResponse(
      AlmoxarifadoMovimentacaoResponse movimentacao, AlmoxarifadoItemResponse item) {
    this.movimentacao = movimentacao;
    this.item = item;
  }

  public AlmoxarifadoMovimentacaoResponse getMovimentacao() {
    return movimentacao;
  }

  public AlmoxarifadoItemResponse getItem() {
    return item;
  }
}
