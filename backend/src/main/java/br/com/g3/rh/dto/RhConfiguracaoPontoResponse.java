package br.com.g3.rh.dto;

import java.time.LocalDateTime;

public class RhConfiguracaoPontoResponse {
  private Long id;
  private Integer cargaSemanalMinutos;
  private Integer cargaSegQuiMinutos;
  private Integer cargaSextaMinutos;
  private Integer cargaSabadoMinutos;
  private Integer cargaDomingoMinutos;
  private Integer toleranciaMinutos;
  private LocalDateTime atualizadoEm;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Integer getCargaSemanalMinutos() {
    return cargaSemanalMinutos;
  }

  public void setCargaSemanalMinutos(Integer cargaSemanalMinutos) {
    this.cargaSemanalMinutos = cargaSemanalMinutos;
  }

  public Integer getCargaSegQuiMinutos() {
    return cargaSegQuiMinutos;
  }

  public void setCargaSegQuiMinutos(Integer cargaSegQuiMinutos) {
    this.cargaSegQuiMinutos = cargaSegQuiMinutos;
  }

  public Integer getCargaSextaMinutos() {
    return cargaSextaMinutos;
  }

  public void setCargaSextaMinutos(Integer cargaSextaMinutos) {
    this.cargaSextaMinutos = cargaSextaMinutos;
  }

  public Integer getCargaSabadoMinutos() {
    return cargaSabadoMinutos;
  }

  public void setCargaSabadoMinutos(Integer cargaSabadoMinutos) {
    this.cargaSabadoMinutos = cargaSabadoMinutos;
  }

  public Integer getCargaDomingoMinutos() {
    return cargaDomingoMinutos;
  }

  public void setCargaDomingoMinutos(Integer cargaDomingoMinutos) {
    this.cargaDomingoMinutos = cargaDomingoMinutos;
  }

  public Integer getToleranciaMinutos() {
    return toleranciaMinutos;
  }

  public void setToleranciaMinutos(Integer toleranciaMinutos) {
    this.toleranciaMinutos = toleranciaMinutos;
  }

  public LocalDateTime getAtualizadoEm() {
    return atualizadoEm;
  }

  public void setAtualizadoEm(LocalDateTime atualizadoEm) {
    this.atualizadoEm = atualizadoEm;
  }
}
