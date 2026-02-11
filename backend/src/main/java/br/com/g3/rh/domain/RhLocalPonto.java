package br.com.g3.rh.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "rh_local_ponto")
public class RhLocalPonto {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "nome", nullable = false, length = 150)
  private String nome;

  @Column(name = "endereco")
  private String endereco;

  @Column(name = "latitude", nullable = false)
  private Double latitude;

  @Column(name = "longitude", nullable = false)
  private Double longitude;

  @Column(name = "raio_metros", nullable = false)
  private Integer raioMetros;

  @Column(name = "accuracy_max_metros", nullable = false)
  private Integer accuracyMaxMetros;

  @Column(name = "ativo", nullable = false)
  private boolean ativo = true;

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
