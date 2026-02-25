package br.com.g3.manualsistema.dto;

import java.util.List;

public class ManualSistemaMudancaListaResponse {
  private List<ManualSistemaMudancaResponse> mudancas;

  public ManualSistemaMudancaListaResponse() {}

  public ManualSistemaMudancaListaResponse(List<ManualSistemaMudancaResponse> mudancas) {
    this.mudancas = mudancas;
  }

  public List<ManualSistemaMudancaResponse> getMudancas() {
    return mudancas;
  }

  public void setMudancas(List<ManualSistemaMudancaResponse> mudancas) {
    this.mudancas = mudancas;
  }
}
