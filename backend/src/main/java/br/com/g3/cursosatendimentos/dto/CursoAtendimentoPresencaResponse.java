package br.com.g3.cursosatendimentos.dto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CursoAtendimentoPresencaResponse {
  private LocalDate dataAula;
  private List<CursoAtendimentoPresencaItemResponse> presencas = new ArrayList<>();

  public CursoAtendimentoPresencaResponse() {}

  public CursoAtendimentoPresencaResponse(LocalDate dataAula, List<CursoAtendimentoPresencaItemResponse> presencas) {
    this.dataAula = dataAula;
    this.presencas = presencas;
  }

  public LocalDate getDataAula() {
    return dataAula;
  }

  public void setDataAula(LocalDate dataAula) {
    this.dataAula = dataAula;
  }

  public List<CursoAtendimentoPresencaItemResponse> getPresencas() {
    return presencas;
  }

  public void setPresencas(List<CursoAtendimentoPresencaItemResponse> presencas) {
    this.presencas = presencas;
  }
}
