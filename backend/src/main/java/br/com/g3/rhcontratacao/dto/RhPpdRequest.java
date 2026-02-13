package br.com.g3.rhcontratacao.dto;

public class RhPpdRequest {
  private String cabecalhoJson;
  private String ladoAJson;
  private String ladoBJson;

  public String getCabecalhoJson() {
    return cabecalhoJson;
  }

  public void setCabecalhoJson(String cabecalhoJson) {
    this.cabecalhoJson = cabecalhoJson;
  }

  public String getLadoAJson() {
    return ladoAJson;
  }

  public void setLadoAJson(String ladoAJson) {
    this.ladoAJson = ladoAJson;
  }

  public String getLadoBJson() {
    return ladoBJson;
  }

  public void setLadoBJson(String ladoBJson) {
    this.ladoBJson = ladoBJson;
  }
}
