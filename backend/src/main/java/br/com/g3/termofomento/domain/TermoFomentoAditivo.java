package br.com.g3.termofomento.domain;

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
@Table(name = "termo_fomento_aditivos")
public class TermoFomentoAditivo {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "termo_fomento_id", nullable = false)
  private Long termoFomentoId;

  @Column(name = "tipo_aditivo", nullable = false, length = 60)
  private String tipoAditivo;

  @Column(name = "data_aditivo", nullable = false)
  private LocalDate dataAditivo;

  @Column(name = "nova_data_fim")
  private LocalDate novaDataFim;

  @Column(name = "novo_valor", precision = 14, scale = 2)
  private BigDecimal novoValor;

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

  public Long getTermoFomentoId() {
    return termoFomentoId;
  }

  public void setTermoFomentoId(Long termoFomentoId) {
    this.termoFomentoId = termoFomentoId;
  }

  public String getTipoAditivo() {
    return tipoAditivo;
  }

  public void setTipoAditivo(String tipoAditivo) {
    this.tipoAditivo = tipoAditivo;
  }

  public LocalDate getDataAditivo() {
    return dataAditivo;
  }

  public void setDataAditivo(LocalDate dataAditivo) {
    this.dataAditivo = dataAditivo;
  }

  public LocalDate getNovaDataFim() {
    return novaDataFim;
  }

  public void setNovaDataFim(LocalDate novaDataFim) {
    this.novaDataFim = novaDataFim;
  }

  public BigDecimal getNovoValor() {
    return novoValor;
  }

  public void setNovoValor(BigDecimal novoValor) {
    this.novoValor = novoValor;
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
