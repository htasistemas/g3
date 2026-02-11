package br.com.g3.contabilidade.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class MovimentacaoFinanceiraResponse {
  private Long id;
  private String tipo;
  private String descricao;
  private String contraparte;
  private String categoria;
  private Long contaBancariaId;
  private String contaBancariaNumero;
  private String contaBancariaBanco;
  private LocalDate dataMovimentacao;
  private BigDecimal valor;
  private Long doacaoId;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

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

  public String getContaBancariaNumero() {
    return contaBancariaNumero;
  }

  public void setContaBancariaNumero(String contaBancariaNumero) {
    this.contaBancariaNumero = contaBancariaNumero;
  }

  public String getContaBancariaBanco() {
    return contaBancariaBanco;
  }

  public void setContaBancariaBanco(String contaBancariaBanco) {
    this.contaBancariaBanco = contaBancariaBanco;
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

  public Long getDoacaoId() {
    return doacaoId;
  }

  public void setDoacaoId(Long doacaoId) {
    this.doacaoId = doacaoId;
  }
}
