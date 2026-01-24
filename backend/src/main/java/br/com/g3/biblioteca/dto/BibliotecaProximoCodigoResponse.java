package br.com.g3.biblioteca.dto;

public class BibliotecaProximoCodigoResponse {
  private String codigo;

  public BibliotecaProximoCodigoResponse(String codigo) {
    this.codigo = codigo;
  }

  public String getCodigo() {
    return codigo;
  }

  public void setCodigo(String codigo) {
    this.codigo = codigo;
  }
}
