package br.com.g3.fotoseventos.dto;

public class FotoEventoFotoAtualizacaoRequest {
  private String legenda;

  private String creditos;

  private java.util.List<String> tags;

  private Integer ordem;

  public String getLegenda() {
    return legenda;
  }

  public void setLegenda(String legenda) {
    this.legenda = legenda;
  }

  public String getCreditos() {
    return creditos;
  }

  public void setCreditos(String creditos) {
    this.creditos = creditos;
  }

  public java.util.List<String> getTags() {
    return tags;
  }

  public void setTags(java.util.List<String> tags) {
    this.tags = tags;
  }

  public Integer getOrdem() {
    return ordem;
  }

  public void setOrdem(Integer ordem) {
    this.ordem = ordem;
  }
}
