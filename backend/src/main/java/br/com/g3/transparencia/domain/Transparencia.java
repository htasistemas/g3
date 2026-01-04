package br.com.g3.transparencia.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transparencia")
public class Transparencia {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "unidade_id")
  private Long unidadeId;

  @Column(name = "total_recebido", precision = 14, scale = 2)
  private BigDecimal totalRecebido;

  @Column(name = "total_recebido_helper", length = 200)
  private String totalRecebidoHelper;

  @Column(name = "total_aplicado", precision = 14, scale = 2)
  private BigDecimal totalAplicado;

  @Column(name = "total_aplicado_helper", length = 200)
  private String totalAplicadoHelper;

  @Column(name = "saldo_disponivel", precision = 14, scale = 2)
  private BigDecimal saldoDisponivel;

  @Column(name = "saldo_disponivel_helper", length = 200)
  private String saldoDisponivelHelper;

  @Column(name = "prestado_mes", precision = 14, scale = 2)
  private BigDecimal prestadoMes;

  @Column(name = "prestado_mes_helper", length = 200)
  private String prestadoMesHelper;

  @Column(name = "criado_em", nullable = false)
  private LocalDateTime criadoEm;

  @Column(name = "atualizado_em", nullable = false)
  private LocalDateTime atualizadoEm;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getUnidadeId() {
    return unidadeId;
  }

  public void setUnidadeId(Long unidadeId) {
    this.unidadeId = unidadeId;
  }

  public BigDecimal getTotalRecebido() {
    return totalRecebido;
  }

  public void setTotalRecebido(BigDecimal totalRecebido) {
    this.totalRecebido = totalRecebido;
  }

  public String getTotalRecebidoHelper() {
    return totalRecebidoHelper;
  }

  public void setTotalRecebidoHelper(String totalRecebidoHelper) {
    this.totalRecebidoHelper = totalRecebidoHelper;
  }

  public BigDecimal getTotalAplicado() {
    return totalAplicado;
  }

  public void setTotalAplicado(BigDecimal totalAplicado) {
    this.totalAplicado = totalAplicado;
  }

  public String getTotalAplicadoHelper() {
    return totalAplicadoHelper;
  }

  public void setTotalAplicadoHelper(String totalAplicadoHelper) {
    this.totalAplicadoHelper = totalAplicadoHelper;
  }

  public BigDecimal getSaldoDisponivel() {
    return saldoDisponivel;
  }

  public void setSaldoDisponivel(BigDecimal saldoDisponivel) {
    this.saldoDisponivel = saldoDisponivel;
  }

  public String getSaldoDisponivelHelper() {
    return saldoDisponivelHelper;
  }

  public void setSaldoDisponivelHelper(String saldoDisponivelHelper) {
    this.saldoDisponivelHelper = saldoDisponivelHelper;
  }

  public BigDecimal getPrestadoMes() {
    return prestadoMes;
  }

  public void setPrestadoMes(BigDecimal prestadoMes) {
    this.prestadoMes = prestadoMes;
  }

  public String getPrestadoMesHelper() {
    return prestadoMesHelper;
  }

  public void setPrestadoMesHelper(String prestadoMesHelper) {
    this.prestadoMesHelper = prestadoMesHelper;
  }

  public LocalDateTime getCriadoEm() {
    return criadoEm;
  }

  public void setCriadoEm(LocalDateTime criadoEm) {
    this.criadoEm = criadoEm;
  }

  public LocalDateTime getAtualizadoEm() {
    return atualizadoEm;
  }

  public void setAtualizadoEm(LocalDateTime atualizadoEm) {
    this.atualizadoEm = atualizadoEm;
  }
}
