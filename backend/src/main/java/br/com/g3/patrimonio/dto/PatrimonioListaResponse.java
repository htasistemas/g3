package br.com.g3.patrimonio.dto;

import java.util.List;

public class PatrimonioListaResponse {
  private List<PatrimonioResponse> patrimonios;

  public PatrimonioListaResponse(List<PatrimonioResponse> patrimonios) {
    this.patrimonios = patrimonios;
  }

  public List<PatrimonioResponse> getPatrimonios() {
    return patrimonios;
  }

  public void setPatrimonios(List<PatrimonioResponse> patrimonios) {
    this.patrimonios = patrimonios;
  }
}
