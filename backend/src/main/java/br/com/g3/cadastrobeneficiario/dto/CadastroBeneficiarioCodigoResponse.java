package br.com.g3.cadastrobeneficiario.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CadastroBeneficiarioCodigoResponse {
  @JsonProperty("codigo")
  private final String codigo;

  public CadastroBeneficiarioCodigoResponse(String codigo) {
    this.codigo = codigo;
  }

  public String getCodigo() {
    return codigo;
  }
}
