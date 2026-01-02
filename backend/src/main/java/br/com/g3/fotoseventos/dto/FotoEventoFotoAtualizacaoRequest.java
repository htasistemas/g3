package br.com.g3.fotoseventos.dto;

public class FotoEventoFotoAtualizacaoRequest {
  private String legenda;

  private Integer ordem;

  public String getLegenda() {
    return legenda;
  }

  public void setLegenda(String legenda) {
    this.legenda = legenda;
  }

  public Integer getOrdem() {
    return ordem;
  }

  public void setOrdem(Integer ordem) {
    this.ordem = ordem;
  }
}
