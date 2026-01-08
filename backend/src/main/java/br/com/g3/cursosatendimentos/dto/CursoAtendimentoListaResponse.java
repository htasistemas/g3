package br.com.g3.cursosatendimentos.dto;

import java.util.List;

public class CursoAtendimentoListaResponse {
  private final List<CursoAtendimentoResponse> records;

  public CursoAtendimentoListaResponse(List<CursoAtendimentoResponse> records) {
    this.records = records;
  }

  public List<CursoAtendimentoResponse> getRecords() {
    return records;
  }
}
