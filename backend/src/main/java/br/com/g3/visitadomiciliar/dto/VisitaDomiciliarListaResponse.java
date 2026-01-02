package br.com.g3.visitadomiciliar.dto;

import java.util.List;

public class VisitaDomiciliarListaResponse {
  private List<VisitaDomiciliarResponse> visitas;

  public VisitaDomiciliarListaResponse() {}

  public VisitaDomiciliarListaResponse(List<VisitaDomiciliarResponse> visitas) {
    this.visitas = visitas;
  }

  public List<VisitaDomiciliarResponse> getVisitas() {
    return visitas;
  }

  public void setVisitas(List<VisitaDomiciliarResponse> visitas) {
    this.visitas = visitas;
  }
}
