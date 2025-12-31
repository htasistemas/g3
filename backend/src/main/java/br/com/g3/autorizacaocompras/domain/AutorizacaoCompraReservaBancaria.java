package br.com.g3.autorizacaocompras.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "autorizacao_compras_reserva_bancaria")
public class AutorizacaoCompraReservaBancaria {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "autorizacao_compra_id", nullable = false)
  private Long autorizacaoCompraId;

  @Column(name = "conta_bancaria_id", nullable = false)
  private Long contaBancariaId;

  @Column(name = "valor", nullable = false, precision = 14, scale = 2)
  private BigDecimal valor;

  @Column(name = "criado_em", nullable = false)
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
