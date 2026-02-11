package br.com.g3.rh.dto;

public class RhPontoBaterRequest {
  private Long funcionarioId;
  private String tipo;
  private String senha;
  private Double latitude;
  private Double longitude;
  private Double accuracy;

  public Long getFuncionarioId() {
    return funcionarioId;
  }

  public void setFuncionarioId(Long funcionarioId) {
    this.funcionarioId = funcionarioId;
  }

  public String getTipo() {
    return tipo;
  }

  public void setTipo(String tipo) {
    this.tipo = tipo;
  }

  public String getSenha() {
    return senha;
  }

  public void setSenha(String senha) {
    this.senha = senha;
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
}
