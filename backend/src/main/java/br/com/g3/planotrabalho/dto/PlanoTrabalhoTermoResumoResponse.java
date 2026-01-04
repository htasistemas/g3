package br.com.g3.planotrabalho.dto;

public class PlanoTrabalhoTermoResumoResponse {
  private final Long id;
  private final String numero;
  private final String objeto;

  public PlanoTrabalhoTermoResumoResponse(Long id, String numero, String objeto) {
    this.id = id;
    this.numero = numero;
    this.objeto = objeto;
  }

  public Long getId() {
    return id;
  }

  public String getNumero() {
    return numero;
  }

  public String getObjeto() {
    return objeto;
  }
}
