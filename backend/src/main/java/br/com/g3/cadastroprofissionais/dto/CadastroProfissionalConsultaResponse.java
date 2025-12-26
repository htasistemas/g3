package br.com.g3.cadastroprofissionais.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CadastroProfissionalConsultaResponse {
  @JsonProperty("profissional")
  private final CadastroProfissionalResponse profissional;

  public CadastroProfissionalConsultaResponse(CadastroProfissionalResponse profissional) {
    this.profissional = profissional;
  }

  public CadastroProfissionalResponse getProfissional() {
    return profissional;
  }
}
