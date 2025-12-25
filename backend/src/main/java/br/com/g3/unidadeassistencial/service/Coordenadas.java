package br.com.g3.unidadeassistencial.service;

import java.math.BigDecimal;

public class Coordenadas {
  private final BigDecimal latitude;
  private final BigDecimal longitude;

  public Coordenadas(BigDecimal latitude, BigDecimal longitude) {
    this.latitude = latitude;
    this.longitude = longitude;
  }

  public BigDecimal getLatitude() {
    return latitude;
  }

  public BigDecimal getLongitude() {
    return longitude;
  }
}
