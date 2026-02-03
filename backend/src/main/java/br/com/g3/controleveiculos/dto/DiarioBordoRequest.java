package br.com.g3.controleveiculos.dto;

import jakarta.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

public class DiarioBordoRequest {
  private Long veiculoId;

  private LocalDate data;

  private String condutor;

  private LocalTime horarioSaida;

  @PositiveOrZero
  private BigDecimal kmInicial;

  @PositiveOrZero
  private BigDecimal combustivelConsumidoLitros;

  private LocalTime horarioChegada;

  @PositiveOrZero
  private BigDecimal kmFinal;

  private String destino;

  private String observacoes;

  public Long getVeiculoId() {
    return veiculoId;
  }

  public void setVeiculoId(Long veiculoId) {
    this.veiculoId = veiculoId;
  }

  public LocalDate getData() {
    return data;
  }

  public void setData(LocalDate data) {
    this.data = data;
  }

  public String getCondutor() {
    return condutor;
  }

  public void setCondutor(String condutor) {
    this.condutor = condutor;
  }

  public LocalTime getHorarioSaida() {
    return horarioSaida;
  }

  public void setHorarioSaida(LocalTime horarioSaida) {
    this.horarioSaida = horarioSaida;
  }

  public BigDecimal getKmInicial() {
    return kmInicial;
  }

  public void setKmInicial(BigDecimal kmInicial) {
    this.kmInicial = kmInicial;
  }

  public LocalTime getHorarioChegada() {
    return horarioChegada;
  }

  public void setHorarioChegada(LocalTime horarioChegada) {
    this.horarioChegada = horarioChegada;
  }

  public BigDecimal getKmFinal() {
    return kmFinal;
  }

  public void setKmFinal(BigDecimal kmFinal) {
    this.kmFinal = kmFinal;
  }

  public BigDecimal getCombustivelConsumidoLitros() {
    return combustivelConsumidoLitros;
  }

  public void setCombustivelConsumidoLitros(BigDecimal combustivelConsumidoLitros) {
    this.combustivelConsumidoLitros = combustivelConsumidoLitros;
  }

  public String getDestino() {
    return destino;
  }

  public void setDestino(String destino) {
    this.destino = destino;
  }

  public String getObservacoes() {
    return observacoes;
  }

  public void setObservacoes(String observacoes) {
    this.observacoes = observacoes;
  }
}
