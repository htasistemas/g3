package br.com.g3.cursosatendimentos.dto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CursoAtendimentoPresencaRequest {
  private LocalDate dataAula;
  private List<CursoAtendimentoPresencaItemRequest> presencas = new ArrayList<>();

  public LocalDate getDataAula() {
    return dataAula;
  }

  public void setDataAula(LocalDate dataAula) {
    this.dataAula = dataAula;
  }

  public List<CursoAtendimentoPresencaItemRequest> getPresencas() {
    return presencas;
  }

  public void setPresencas(List<CursoAtendimentoPresencaItemRequest> presencas) {
    this.presencas = presencas;
  }
}
