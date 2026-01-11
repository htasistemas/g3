package br.com.g3.emprestimoseventos.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "emprestimos_eventos")
public class EmprestimoEvento {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "evento_id", nullable = false)
  private EventoEmprestimo evento;

  @Column(name = "unidade_id")
  private Long unidadeId;

  @Column(name = "responsavel_id")
  private Long responsavelId;

  @Column(name = "data_retirada_prevista", nullable = false)
  private LocalDateTime dataRetiradaPrevista;

  @Column(name = "data_devolucao_prevista", nullable = false)
  private LocalDateTime dataDevolucaoPrevista;

  @Column(name = "data_retirada_real")
  private LocalDateTime dataRetiradaReal;

  @Column(name = "data_devolucao_real")
  private LocalDateTime dataDevolucaoReal;

  @Column(name = "status", length = 20, nullable = false)
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

  public EventoEmprestimo getEvento() {
    return evento;
  }

  public void setEvento(EventoEmprestimo evento) {
    this.evento = evento;
  }

  public Long getUnidadeId() {
    return unidadeId;
  }

  public void setUnidadeId(Long unidadeId) {
    this.unidadeId = unidadeId;
  }

  public Long getResponsavelId() {
    return responsavelId;
  }

  public void setResponsavelId(Long responsavelId) {
    this.responsavelId = responsavelId;
  }

  public LocalDateTime getDataRetiradaPrevista() {
    return dataRetiradaPrevista;
  }

  public void setDataRetiradaPrevista(LocalDateTime dataRetiradaPrevista) {
    this.dataRetiradaPrevista = dataRetiradaPrevista;
  }

  public LocalDateTime getDataDevolucaoPrevista() {
    return dataDevolucaoPrevista;
  }

  public void setDataDevolucaoPrevista(LocalDateTime dataDevolucaoPrevista) {
    this.dataDevolucaoPrevista = dataDevolucaoPrevista;
  }

  public LocalDateTime getDataRetiradaReal() {
    return dataRetiradaReal;
  }

  public void setDataRetiradaReal(LocalDateTime dataRetiradaReal) {
    this.dataRetiradaReal = dataRetiradaReal;
  }

  public LocalDateTime getDataDevolucaoReal() {
    return dataDevolucaoReal;
  }

  public void setDataDevolucaoReal(LocalDateTime dataDevolucaoReal) {
    this.dataDevolucaoReal = dataDevolucaoReal;
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
