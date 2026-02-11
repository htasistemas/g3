package br.com.g3.rh.dto;

import java.time.LocalDateTime;

public class RhLocalPontoResponse {
  private Long id;
  private String nome;
  private String endereco;
  private Double latitude;
  private Double longitude;
  private Integer raioMetros;
  private Integer accuracyMaxMetros;
  private boolean ativo;
  private LocalDateTime criadoEm;
  private LocalDateTime atualizadoEm;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getNome() {
    return nome;
  }

  public void setNome(String nome) {
    this.nome = nome;
  }

  public String getEndereco() {
    return endereco;
  }

  public void setEndereco(String endereco) {
    this.endereco = endereco;
  }

  public Double getLatitude() {
    return latitude;
  }

  public void setLatitude(Double latitude) {
    this.latitude = latitude;
  }

  public Double getLongitude() {
    return longitude;
  }

  public void setLongitude(Double longitude) {
    this.longitude = longitude;
  }

  public Integer getRaioMetros() {
    return raioMetros;
  }

  public void setRaioMetros(Integer raioMetros) {
    this.raioMetros = raioMetros;
  }

  public Integer getAccuracyMaxMetros() {
    return accuracyMaxMetros;
  }

  public void setAccuracyMaxMetros(Integer accuracyMaxMetros) {
    this.accuracyMaxMetros = accuracyMaxMetros;
  }

  public boolean isAtivo() {
    return ativo;
  }

  public void setAtivo(boolean ativo) {
    this.ativo = ativo;
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
