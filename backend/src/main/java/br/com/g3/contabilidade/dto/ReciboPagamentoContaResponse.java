package br.com.g3.contabilidade.dto;

import java.math.BigDecimal;

public class ReciboPagamentoContaResponse {
  private Long contaBancariaId;
  private String banco;
  private String numero;
  private BigDecimal valor;

  public Long getContaBancariaId() {
    return contaBancariaId;
  }

  public void setContaBancariaId(Long contaBancariaId) {
    this.contaBancariaId = contaBancariaId;
  }

  public String getBanco() {
    return banco;
  }

  public void setBanco(String banco) {
    this.banco = banco;
  }

  public String getNumero() {
    return numero;
  }

  public void setNumero(String numero) {
    this.numero = numero;
  }

  public BigDecimal getValor() {
    return valor;
  }

  public void setValor(BigDecimal valor) {
    this.valor = valor;
  }
}
