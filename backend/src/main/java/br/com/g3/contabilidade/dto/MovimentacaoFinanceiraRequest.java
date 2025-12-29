package br.com.g3.contabilidade.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class MovimentacaoFinanceiraRequest {
  private String tipo;
  private String descricao;
  private String contraparte;
  private String categoria;
  private Long contaBancariaId;
  private LocalDate dataMovimentacao;
  private BigDecimal valor;

  public String getTipo() {
    return tipo;
  }

  public void setTipo(String tipo) {
    this.tipo = tipo;
  }

  public String getDescricao() {
    return descricao;
  }

  public void setDescricao(String descricao) {
    this.descricao = descricao;
  }

  public String getContraparte() {
    return contraparte;
  }

  public void setContraparte(String contraparte) {
    this.contraparte = contraparte;
  }

  public String getCategoria() {
    return categoria;
  }

  public void setCategoria(String categoria) {
    this.categoria = categoria;
  }

  public Long getContaBancariaId() {
    return contaBancariaId;
  }

  public void setContaBancariaId(Long contaBancariaId) {
    this.contaBancariaId = contaBancariaId;
  }

  public LocalDate getDataMovimentacao() {
    return dataMovimentacao;
  }

  public void setDataMovimentacao(LocalDate dataMovimentacao) {
    this.dataMovimentacao = dataMovimentacao;
  }

  public BigDecimal getValor() {
    return valor;
  }

  public void setValor(BigDecimal valor) {
    this.valor = valor;
  }
}
