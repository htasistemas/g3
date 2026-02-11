package br.com.g3.recebimentodoacao.dto;

import java.math.BigDecimal;

public class RecebimentoDoacaoItemResponse {
  private Long id;
  private String descricao;
  private Integer quantidade;
  private String unidade;
  private BigDecimal valorUnitario;
  private BigDecimal valorTotal;
  private String marca;
  private String modelo;
  private String conservacao;
  private String observacoes;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getDescricao() {
    return descricao;
  }

  public void setDescricao(String descricao) {
    this.descricao = descricao;
  }

  public Integer getQuantidade() {
    return quantidade;
  }

  public void setQuantidade(Integer quantidade) {
    this.quantidade = quantidade;
  }

  public String getUnidade() {
    return unidade;
  }

  public void setUnidade(String unidade) {
    this.unidade = unidade;
  }

  public BigDecimal getValorUnitario() {
    return valorUnitario;
  }

  public void setValorUnitario(BigDecimal valorUnitario) {
    this.valorUnitario = valorUnitario;
  }

  public BigDecimal getValorTotal() {
    return valorTotal;
  }

  public void setValorTotal(BigDecimal valorTotal) {
    this.valorTotal = valorTotal;
  }

  public String getMarca() {
    return marca;
  }

  public void setMarca(String marca) {
    this.marca = marca;
  }

  public String getModelo() {
    return modelo;
  }

  public void setModelo(String modelo) {
    this.modelo = modelo;
  }

  public String getConservacao() {
    return conservacao;
  }

  public void setConservacao(String conservacao) {
    this.conservacao = conservacao;
  }

  public String getObservacoes() {
    return observacoes;
  }

  public void setObservacoes(String observacoes) {
    this.observacoes = observacoes;
  }
}
