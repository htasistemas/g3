package br.com.g3.rh.dto;

import java.time.LocalDateTime;

public class RhPontoMarcacaoResponse {
  private Long id;
  private String tipo;
  private LocalDateTime dataHoraServidor;
  private Double latitude;
  private Double longitude;
  private Double accuracy;
  private Double distanciaMetros;
  private boolean dentroPerimetro;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
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
}
