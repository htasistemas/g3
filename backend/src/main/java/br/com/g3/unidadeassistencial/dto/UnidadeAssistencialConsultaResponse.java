package br.com.g3.unidadeassistencial.dto;

public class UnidadeAssistencialConsultaResponse {
  private final UnidadeAssistencialResponse unidade;

  public UnidadeAssistencialConsultaResponse(UnidadeAssistencialResponse unidade) {
    this.unidade = unidade;
  }

  public UnidadeAssistencialResponse getUnidade() {
    return unidade;
  }
}
