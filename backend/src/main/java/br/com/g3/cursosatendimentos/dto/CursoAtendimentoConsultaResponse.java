package br.com.g3.cursosatendimentos.dto;

public class CursoAtendimentoConsultaResponse {
  private final CursoAtendimentoResponse record;

  public CursoAtendimentoConsultaResponse(CursoAtendimentoResponse record) {
    this.record = record;
  }

  public CursoAtendimentoResponse getRecord() {
    return record;
  }
}
