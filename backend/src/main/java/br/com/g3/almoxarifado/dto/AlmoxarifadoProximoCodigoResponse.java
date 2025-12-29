package br.com.g3.almoxarifado.dto;

public class AlmoxarifadoProximoCodigoResponse {
  private final String codigo;

  public AlmoxarifadoProximoCodigoResponse(String codigo) {
    this.codigo = codigo;
  }

  public String getCodigo() {
    return codigo;
  }
}
