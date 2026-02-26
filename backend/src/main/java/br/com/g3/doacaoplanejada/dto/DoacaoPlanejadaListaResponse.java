package br.com.g3.doacaoplanejada.dto;

import java.util.ArrayList;
import java.util.List;

public class DoacaoPlanejadaListaResponse {
  private List<DoacaoPlanejadaResponse> doacoesPlanejadas = new ArrayList<>();

  public DoacaoPlanejadaListaResponse() {}

  public DoacaoPlanejadaListaResponse(List<DoacaoPlanejadaResponse> doacoesPlanejadas) {
    this.doacoesPlanejadas = doacoesPlanejadas;
  }

  public List<DoacaoPlanejadaResponse> getDoacoesPlanejadas() {
    return doacoesPlanejadas;
  }

  public void setDoacoesPlanejadas(List<DoacaoPlanejadaResponse> doacoesPlanejadas) {
    this.doacoesPlanejadas = doacoesPlanejadas;
  }
}
