package br.com.g3.contabilidade.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "conta_bancaria")
public class ContaBancaria {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "banco", nullable = false, length = 120)
  private String banco;

  @Column(name = "agencia", length = 20)
  private String agencia;

  @Column(name = "numero", nullable = false, length = 40)
  private String numero;

  @Column(name = "tipo", nullable = false, length = 30)
  private String tipo;

  @Column(name = "projeto_vinculado", length = 200)
  private String projetoVinculado;

  @Column(name = "pix_vinculado", nullable = false)
  private Boolean pixVinculado = false;

  @Column(name = "tipo_chave_pix", length = 20)
  private String tipoChavePix;

  @Column(name = "chave_pix", length = 200)
  private String chavePix;

  @Column(name = "saldo", nullable = false, precision = 12, scale = 2)
  private BigDecimal saldo;

  @Column(name = "data_atualizacao", nullable = false)
  private LocalDate dataAtualizacao;

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

  public String getBanco() {
    return banco;
  }

  public void setBanco(String banco) {
    this.banco = banco;
  }

  public String getAgencia() {
    return agencia;
  }

  public void setAgencia(String agencia) {
    this.agencia = agencia;
  }

  public String getNumero() {
    return numero;
  }

  public void setNumero(String numero) {
    this.numero = numero;
  }

  public String getTipo() {
    return tipo;
  }

  public void setTipo(String tipo) {
    this.tipo = tipo;
  }

  public String getProjetoVinculado() {
    return projetoVinculado;
  }

  public void setProjetoVinculado(String projetoVinculado) {
    this.projetoVinculado = projetoVinculado;
  }

  public Boolean getPixVinculado() {
    return pixVinculado;
  }

  public void setPixVinculado(Boolean pixVinculado) {
    this.pixVinculado = pixVinculado;
  }

  public String getTipoChavePix() {
    return tipoChavePix;
  }

  public void setTipoChavePix(String tipoChavePix) {
    this.tipoChavePix = tipoChavePix;
  }

  public String getChavePix() {
    return chavePix;
  }

  public void setChavePix(String chavePix) {
    this.chavePix = chavePix;
  }

  public BigDecimal getSaldo() {
    return saldo;
  }

  public void setSaldo(BigDecimal saldo) {
    this.saldo = saldo;
  }

  public LocalDate getDataAtualizacao() {
    return dataAtualizacao;
  }

  public void setDataAtualizacao(LocalDate dataAtualizacao) {
    this.dataAtualizacao = dataAtualizacao;
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
