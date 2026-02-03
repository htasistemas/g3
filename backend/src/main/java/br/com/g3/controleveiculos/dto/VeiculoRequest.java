package br.com.g3.controleveiculos.dto;

import jakarta.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;

public class VeiculoRequest {
  private String placa;

  private String modelo;

  private String marca;

  @PositiveOrZero
  private Integer ano;

  private String tipoCombustivel;

  @PositiveOrZero
  private BigDecimal mediaConsumoPadrao;

  private BigDecimal capacidadeTanqueLitros;

  private String observacoes;

  private Boolean ativo;

  private String fotoFrente;

  private String fotoLateralEsquerda;

  private String fotoLateralDireita;

  private String fotoTraseira;

  public String getPlaca() {
    return placa;
  }

  public void setPlaca(String placa) {
    this.placa = placa;
  }

  public String getModelo() {
    return modelo;
  }

  public void setModelo(String modelo) {
    this.modelo = modelo;
  }

  public String getMarca() {
    return marca;
  }

  public void setMarca(String marca) {
    this.marca = marca;
  }

  public Integer getAno() {
    return ano;
  }

  public void setAno(Integer ano) {
    this.ano = ano;
  }

  public String getTipoCombustivel() {
    return tipoCombustivel;
  }

  public void setTipoCombustivel(String tipoCombustivel) {
    this.tipoCombustivel = tipoCombustivel;
  }

  public BigDecimal getMediaConsumoPadrao() {
    return mediaConsumoPadrao;
  }

  public void setMediaConsumoPadrao(BigDecimal mediaConsumoPadrao) {
    this.mediaConsumoPadrao = mediaConsumoPadrao;
  }

  public BigDecimal getCapacidadeTanqueLitros() {
    return capacidadeTanqueLitros;
  }

  public void setCapacidadeTanqueLitros(BigDecimal capacidadeTanqueLitros) {
    this.capacidadeTanqueLitros = capacidadeTanqueLitros;
  }

  public String getObservacoes() {
    return observacoes;
  }

  public void setObservacoes(String observacoes) {
    this.observacoes = observacoes;
  }

  public Boolean getAtivo() {
    return ativo;
  }

  public void setAtivo(Boolean ativo) {
    this.ativo = ativo;
  }

  public String getFotoFrente() {
    return fotoFrente;
  }

  public void setFotoFrente(String fotoFrente) {
    this.fotoFrente = fotoFrente;
  }

  public String getFotoLateralEsquerda() {
    return fotoLateralEsquerda;
  }

  public void setFotoLateralEsquerda(String fotoLateralEsquerda) {
    this.fotoLateralEsquerda = fotoLateralEsquerda;
  }

  public String getFotoLateralDireita() {
    return fotoLateralDireita;
  }

  public void setFotoLateralDireita(String fotoLateralDireita) {
    this.fotoLateralDireita = fotoLateralDireita;
  }

  public String getFotoTraseira() {
    return fotoTraseira;
  }

  public void setFotoTraseira(String fotoTraseira) {
    this.fotoTraseira = fotoTraseira;
  }
}
