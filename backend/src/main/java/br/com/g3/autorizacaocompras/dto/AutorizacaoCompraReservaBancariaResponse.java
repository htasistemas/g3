package br.com.g3.autorizacaocompras.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class AutorizacaoCompraReservaBancariaResponse {
  private Long id;
  private Long autorizacaoCompraId;
  private Long contaBancariaId;
  private BigDecimal valor;
  private LocalDateTime criadoEm;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getAutorizacaoCompraId() {
    return autorizacaoCompraId;
  }

  public void setAutorizacaoCompraId(Long autorizacaoCompraId) {
    this.autorizacaoCompraId = autorizacaoCompraId;
  }

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

  public LocalDateTime getCriadoEm() {
    return criadoEm;
  }

  public void setCriadoEm(LocalDateTime criadoEm) {
    this.criadoEm = criadoEm;
  }
}
