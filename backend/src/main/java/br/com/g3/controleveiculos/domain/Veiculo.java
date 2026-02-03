package br.com.g3.controleveiculos.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "controle_veiculos")
public class Veiculo {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "placa", length = 20)
  private String placa;

  @Column(name = "modelo", length = 120)
  private String modelo;

  @Column(name = "marca", length = 120)
  private String marca;

  @Column(name = "ano")
  private Integer ano;

  @Column(name = "tipo_combustivel", length = 60)
  private String tipoCombustivel;

  @Column(name = "media_consumo_padrao", precision = 12, scale = 2)
  private BigDecimal mediaConsumoPadrao;

  @Column(name = "capacidade_tanque_litros", precision = 10, scale = 2)
  private BigDecimal capacidadeTanqueLitros;

  @Column(name = "observacoes", columnDefinition = "TEXT")
  private String observacoes;

  @Column(name = "ativo")
  private Boolean ativo;

  @Column(name = "foto_frente", columnDefinition = "TEXT")
  private String fotoFrente;

  @Column(name = "foto_lateral_esquerda", columnDefinition = "TEXT")
  private String fotoLateralEsquerda;

  @Column(name = "foto_lateral_direita", columnDefinition = "TEXT")
  private String fotoLateralDireita;

  @Column(name = "foto_traseira", columnDefinition = "TEXT")
  private String fotoTraseira;

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
