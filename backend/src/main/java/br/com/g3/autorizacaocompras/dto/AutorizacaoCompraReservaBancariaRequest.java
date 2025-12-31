package br.com.g3.autorizacaocompras.dto;

import java.math.BigDecimal;

public class AutorizacaoCompraReservaBancariaRequest {
  private Long contaBancariaId;
  private BigDecimal valor;

  public Long getContaBancariaId() {
    return contaBancariaId;
  }

  public void setContaBancariaId(Long contaBancariaId) {
    this.contaBancariaId = contaBancariaId;
  }

  public BigDecimal getValor() {
    return valor;
  }

  public void setValor(BigDecimal valor) {
    this.valor = valor;
  }
}
