package br.com.g3.controleveiculos.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class DiarioBordoResponse {
  private Long id;
  private Long veiculoId;
  private LocalDate data;
  private String condutor;
  private LocalTime horarioSaida;
  private BigDecimal kmInicial;
  private LocalTime horarioChegada;
  private BigDecimal kmFinal;
  private String destino;
  private BigDecimal combustivelConsumidoLitros;
  private BigDecimal kmRodados;
  private BigDecimal mediaConsumo;
  private String observacoes;
  private LocalDateTime criadoEm;
  private LocalDateTime atualizadoEm;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

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

  public String getDestino() {
    return destino;
  }

  public void setDestino(String destino) {
    this.destino = destino;
  }

  public BigDecimal getCombustivelConsumidoLitros() {
    return combustivelConsumidoLitros;
  }

  public void setCombustivelConsumidoLitros(BigDecimal combustivelConsumidoLitros) {
    this.combustivelConsumidoLitros = combustivelConsumidoLitros;
  }

  public BigDecimal getKmRodados() {
    return kmRodados;
  }

  public void setKmRodados(BigDecimal kmRodados) {
    this.kmRodados = kmRodados;
  }

  public BigDecimal getMediaConsumo() {
    return mediaConsumo;
  }

  public void setMediaConsumo(BigDecimal mediaConsumo) {
    this.mediaConsumo = mediaConsumo;
  }

  public String getObservacoes() {
    return observacoes;
  }

  public void setObservacoes(String observacoes) {
    this.observacoes = observacoes;
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
