package br.com.g3.patrimonio.dto;

import jakarta.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.time.LocalDate;

public class PatrimonioRequest {
  @NotBlank
  private String numeroPatrimonio;

  @NotBlank
  private String nome;

  private String categoria;
  private String subcategoria;
  private String conservacao;
  private String status;
  private LocalDate dataAquisicao;
  private BigDecimal valorAquisicao;
  private String origem;
  private String responsavel;
  private String unidade;
  private String sala;
  private BigDecimal taxaDepreciacao;
  private String observacoes;

  public String getNumeroPatrimonio() {
    return numeroPatrimonio;
  }

  public void setNumeroPatrimonio(String numeroPatrimonio) {
    this.numeroPatrimonio = numeroPatrimonio;
  }

  public String getNome() {
    return nome;
  }

  public void setNome(String nome) {
    this.nome = nome;
  }

  public String getCategoria() {
    return categoria;
  }

  public void setCategoria(String categoria) {
    this.categoria = categoria;
  }

  public String getSubcategoria() {
    return subcategoria;
  }

  public void setSubcategoria(String subcategoria) {
    this.subcategoria = subcategoria;
  }

  public String getConservacao() {
    return conservacao;
  }

  public void setConservacao(String conservacao) {
    this.conservacao = conservacao;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public LocalDate getDataAquisicao() {
    return dataAquisicao;
  }

  public void setDataAquisicao(LocalDate dataAquisicao) {
    this.dataAquisicao = dataAquisicao;
  }

  public BigDecimal getValorAquisicao() {
    return valorAquisicao;
  }

  public void setValorAquisicao(BigDecimal valorAquisicao) {
    this.valorAquisicao = valorAquisicao;
  }

  public String getOrigem() {
    return origem;
  }

  public void setOrigem(String origem) {
    this.origem = origem;
  }

  public String getResponsavel() {
    return responsavel;
  }

  public void setResponsavel(String responsavel) {
    this.responsavel = responsavel;
  }

  public String getUnidade() {
    return unidade;
  }

  public void setUnidade(String unidade) {
    this.unidade = unidade;
  }

  public String getSala() {
    return sala;
  }

  public void setSala(String sala) {
    this.sala = sala;
  }

  public BigDecimal getTaxaDepreciacao() {
    return taxaDepreciacao;
  }

  public void setTaxaDepreciacao(BigDecimal taxaDepreciacao) {
    this.taxaDepreciacao = taxaDepreciacao;
  }

  public String getObservacoes() {
    return observacoes;
  }

  public void setObservacoes(String observacoes) {
    this.observacoes = observacoes;
  }
}
