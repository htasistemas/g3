package br.com.g3.contabilidade.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "emenda_impositiva")
public class EmendaImpositiva {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "identificacao", nullable = false, length = 120)
  private String identificacao;

  @Column(name = "referencia_legal", length = 160)
  private String referenciaLegal;

  @Column(name = "data_prevista", nullable = false)
  private LocalDate dataPrevista;

  @Column(name = "valor_previsto", nullable = false, precision = 12, scale = 2)
  private BigDecimal valorPrevisto;

  @Column(name = "dias_alerta", nullable = false)
  private Integer diasAlerta;

  @Column(name = "status", nullable = false, length = 20)
  private String status;

  @Column(name = "observacoes")
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

  public String getIdentificacao() {
    return identificacao;
  }

  public void setIdentificacao(String identificacao) {
    this.identificacao = identificacao;
  }

  public String getReferenciaLegal() {
    return referenciaLegal;
  }

  public void setReferenciaLegal(String referenciaLegal) {
    this.referenciaLegal = referenciaLegal;
  }

  public LocalDate getDataPrevista() {
    return dataPrevista;
  }

  public void setDataPrevista(LocalDate dataPrevista) {
    this.dataPrevista = dataPrevista;
  }

  public BigDecimal getValorPrevisto() {
    return valorPrevisto;
  }

  public void setValorPrevisto(BigDecimal valorPrevisto) {
    this.valorPrevisto = valorPrevisto;
  }

  public Integer getDiasAlerta() {
    return diasAlerta;
  }

  public void setDiasAlerta(Integer diasAlerta) {
    this.diasAlerta = diasAlerta;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
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
