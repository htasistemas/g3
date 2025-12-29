package br.com.g3.contabilidade.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class ContaBancariaResponse {
  private Long id;
  private String banco;
  private String agencia;
  private String numero;
  private String tipo;
  private String projetoVinculado;
  private Boolean pixVinculado;
  private String tipoChavePix;
  private String chavePix;
  private BigDecimal saldo;
  private LocalDate dataAtualizacao;

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
}
