package br.com.g3.rh.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "rh_ponto_marcacao")
public class RhPontoMarcacao {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "ponto_dia_id", nullable = false)
  private RhPontoDia pontoDia;

  @Column(name = "tipo", nullable = false, length = 2)
  private String tipo;

  @Column(name = "data_hora_servidor", nullable = false)
  private LocalDateTime dataHoraServidor;

  @Column(name = "latitude", nullable = false)
  private Double latitude;

  @Column(name = "longitude", nullable = false)
  private Double longitude;

  @Column(name = "accuracy", nullable = false)
  private Double accuracy;

  @Column(name = "distancia_metros", nullable = false)
  private Double distanciaMetros;

  @Column(name = "dentro_perimetro", nullable = false)
  private boolean dentroPerimetro;

  @Column(name = "ip", length = 60)
  private String ip;

  @Column(name = "user_agent", length = 255)
  private String userAgent;

  @Column(name = "criado_em", nullable = false)
  private LocalDateTime criadoEm;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public RhPontoDia getPontoDia() {
    return pontoDia;
  }

  public void setPontoDia(RhPontoDia pontoDia) {
    this.pontoDia = pontoDia;
  }

  public String getTipo() {
    return tipo;
  }

  public void setTipo(String tipo) {
    this.tipo = tipo;
  }

  public LocalDateTime getDataHoraServidor() {
    return dataHoraServidor;
  }

  public void setDataHoraServidor(LocalDateTime dataHoraServidor) {
    this.dataHoraServidor = dataHoraServidor;
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

  public Double getAccuracy() {
    return accuracy;
  }

  public void setAccuracy(Double accuracy) {
    this.accuracy = accuracy;
  }

  public Double getDistanciaMetros() {
    return distanciaMetros;
  }

  public void setDistanciaMetros(Double distanciaMetros) {
    this.distanciaMetros = distanciaMetros;
  }

  public boolean isDentroPerimetro() {
    return dentroPerimetro;
  }

  public void setDentroPerimetro(boolean dentroPerimetro) {
    this.dentroPerimetro = dentroPerimetro;
  }

  public String getIp() {
    return ip;
  }

  public void setIp(String ip) {
    this.ip = ip;
  }

  public String getUserAgent() {
    return userAgent;
  }

  public void setUserAgent(String userAgent) {
    this.userAgent = userAgent;
  }

  public LocalDateTime getCriadoEm() {
    return criadoEm;
  }

  public void setCriadoEm(LocalDateTime criadoEm) {
    this.criadoEm = criadoEm;
  }
}
