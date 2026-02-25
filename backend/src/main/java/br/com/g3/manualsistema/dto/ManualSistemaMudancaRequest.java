package br.com.g3.manualsistema.dto;

import java.time.LocalDateTime;

public class ManualSistemaMudancaRequest {
  private LocalDateTime dataMudanca;
  private String autor;
  private String modulo;
  private String tela;
  private String tipo;
  private String descricaoCurta;
  private String descricaoDetalhada;
  private String versaoBuild;
  private String links;

  public LocalDateTime getDataMudanca() {
    return dataMudanca;
  }

  public void setDataMudanca(LocalDateTime dataMudanca) {
    this.dataMudanca = dataMudanca;
  }

  public String getAutor() {
    return autor;
  }

  public void setAutor(String autor) {
    this.autor = autor;
  }

  public String getModulo() {
    return modulo;
  }

  public void setModulo(String modulo) {
    this.modulo = modulo;
  }

  public String getTela() {
    return tela;
  }

  public void setTela(String tela) {
    this.tela = tela;
  }

  public String getTipo() {
    return tipo;
  }

  public void setTipo(String tipo) {
    this.tipo = tipo;
  }

  public String getDescricaoCurta() {
    return descricaoCurta;
  }

  public void setDescricaoCurta(String descricaoCurta) {
    this.descricaoCurta = descricaoCurta;
  }

  public String getDescricaoDetalhada() {
    return descricaoDetalhada;
  }

  public void setDescricaoDetalhada(String descricaoDetalhada) {
    this.descricaoDetalhada = descricaoDetalhada;
  }

  public String getVersaoBuild() {
    return versaoBuild;
  }

  public void setVersaoBuild(String versaoBuild) {
    this.versaoBuild = versaoBuild;
  }

  public String getLinks() {
    return links;
  }

  public void setLinks(String links) {
    this.links = links;
  }
}
