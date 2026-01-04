package br.com.g3.transparencia.dto;

import java.math.BigDecimal;

public class TransparenciaRecebimentoResponse {
  private final Long id;
  private final String fonte;
  private final BigDecimal valor;
  private final String periodicidade;
  private final String status;

  public TransparenciaRecebimentoResponse(
      Long id,
      String fonte,
      BigDecimal valor,
      String periodicidade,
      String status) {
    this.id = id;
    this.fonte = fonte;
    this.valor = valor;
    this.periodicidade = periodicidade;
    this.status = status;
  }

  public Long getId() {
    return id;
  }

  public String getFonte() {
    return fonte;
  }

  public BigDecimal getValor() {
    return valor;
  }

  public String getPeriodicidade() {
    return periodicidade;
  }

  public String getStatus() {
    return status;
  }
}
