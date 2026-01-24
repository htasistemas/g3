package br.com.g3.controleveiculos.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "controle_veiculos_diario")
public class DiarioBordo {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "veiculo_id", nullable = false)
  private Long veiculoId;

  @Column(name = "data", nullable = false)
  private LocalDate data;

  @Column(name = "condutor", length = 150, nullable = false)
  private String condutor;

  @Column(name = "horario_saida", nullable = false)
  private LocalTime horarioSaida;

  @Column(name = "km_inicial", precision = 12, scale = 2, nullable = false)
  private BigDecimal kmInicial;

  @Column(name = "horario_chegada", nullable = false)
  private LocalTime horarioChegada;

  @Column(name = "km_final", precision = 12, scale = 2, nullable = false)
  private BigDecimal kmFinal;

  @Column(name = "destino", length = 200, nullable = false)
  private String destino;

  @Column(name = "combustivel_consumido_litros", precision = 12, scale = 2, nullable = false)
  private BigDecimal combustivelConsumidoLitros;

  @Column(name = "km_rodados", precision = 12, scale = 2, nullable = false)
  private BigDecimal kmRodados;

  @Column(name = "media_consumo", precision = 12, scale = 2, nullable = false)
  private BigDecimal mediaConsumo;

  @Column(name = "observacoes", columnDefinition = "TEXT")
  private String observacoes;

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
