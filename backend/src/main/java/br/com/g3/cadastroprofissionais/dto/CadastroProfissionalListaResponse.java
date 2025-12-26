package br.com.g3.cadastroprofissionais.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class CadastroProfissionalListaResponse {
  @JsonProperty("profissionais")
  private final List<CadastroProfissionalResponse> profissionais;

  public CadastroProfissionalListaResponse(List<CadastroProfissionalResponse> profissionais) {
    this.profissionais = profissionais;
  }

  public List<CadastroProfissionalResponse> getProfissionais() {
    return profissionais;
  }
}
