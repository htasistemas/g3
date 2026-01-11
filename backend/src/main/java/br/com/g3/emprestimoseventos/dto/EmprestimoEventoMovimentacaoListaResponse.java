package br.com.g3.emprestimoseventos.dto;

import java.util.List;

public class EmprestimoEventoMovimentacaoListaResponse {
  private List<EmprestimoEventoMovimentacaoResponse> movimentacoes;

  public EmprestimoEventoMovimentacaoListaResponse(
      List<EmprestimoEventoMovimentacaoResponse> movimentacoes) {
    this.movimentacoes = movimentacoes;
  }

  public List<EmprestimoEventoMovimentacaoResponse> getMovimentacoes() {
    return movimentacoes;
  }

  public void setMovimentacoes(List<EmprestimoEventoMovimentacaoResponse> movimentacoes) {
    this.movimentacoes = movimentacoes;
  }
}
