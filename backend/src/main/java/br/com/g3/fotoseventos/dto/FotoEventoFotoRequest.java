package br.com.g3.fotoseventos.dto;

import jakarta.validation.Valid;

public class FotoEventoFotoRequest {
  @Valid
  private FotoEventoUploadRequest arquivo;

  private String legenda;

  private Integer ordem;

  public FotoEventoUploadRequest getArquivo() {
    return arquivo;
  }

  public void setArquivo(FotoEventoUploadRequest arquivo) {
    this.arquivo = arquivo;
  }

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
