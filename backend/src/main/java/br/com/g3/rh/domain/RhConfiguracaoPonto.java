package br.com.g3.rh.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "rh_configuracao_ponto")
public class RhConfiguracaoPonto {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "carga_semanal_minutos", nullable = false)
  private Integer cargaSemanalMinutos;

  @Column(name = "carga_seg_qui_minutos", nullable = false)
  private Integer cargaSegQuiMinutos;

  @Column(name = "carga_sexta_minutos", nullable = false)
  private Integer cargaSextaMinutos;

  @Column(name = "carga_sabado_minutos", nullable = false)
  private Integer cargaSabadoMinutos;

  @Column(name = "carga_domingo_minutos", nullable = false)
  private Integer cargaDomingoMinutos;

  @Column(name = "tolerancia_minutos", nullable = false)
  private Integer toleranciaMinutos;

  @Column(name = "atualizado_em", nullable = false)
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
