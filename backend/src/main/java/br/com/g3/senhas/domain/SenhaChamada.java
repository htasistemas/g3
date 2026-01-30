package br.com.g3.senhas.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "senha_chamada")
public class SenhaChamada {
  @Id
  @Column(name = "id", nullable = false)
  private UUID id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "fila_id", nullable = false)
  private SenhaFila fila;

  @Column(name = "nome_beneficiario", nullable = false, length = 200)
  private String nomeBeneficiario;

  @Column(name = "local_atendimento", nullable = false, length = 120)
  private String localAtendimento;

  @Column(name = "status", nullable = false, length = 20)
  private String status;

  @Column(name = "data_hora_chamada", nullable = false)
  private LocalDateTime dataHoraChamada;

  @Column(name = "data_hora_criacao", nullable = false)
  private LocalDateTime dataHoraCriacao;

  @Column(name = "chamado_por", nullable = false, length = 120)
  private String chamadoPor;

  @Column(name = "unidade_id")
  private Long unidadeId;

  @Column(name = "usuario_id")
  private Long usuarioId;

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public SenhaFila getFila() {
    return fila;
  }

  public void setFila(SenhaFila fila) {
    this.fila = fila;
  }

  public String getNomeBeneficiario() {
    return nomeBeneficiario;
  }

  public void setNomeBeneficiario(String nomeBeneficiario) {
    this.nomeBeneficiario = nomeBeneficiario;
  }

  public String getLocalAtendimento() {
    return localAtendimento;
  }

  public void setLocalAtendimento(String localAtendimento) {
    this.localAtendimento = localAtendimento;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public LocalDateTime getDataHoraChamada() {
    return dataHoraChamada;
  }

  public void setDataHoraChamada(LocalDateTime dataHoraChamada) {
    this.dataHoraChamada = dataHoraChamada;
  }

  public LocalDateTime getDataHoraCriacao() {
    return dataHoraCriacao;
  }

  public void setDataHoraCriacao(LocalDateTime dataHoraCriacao) {
    this.dataHoraCriacao = dataHoraCriacao;
  }

  public String getChamadoPor() {
    return chamadoPor;
  }

  public void setChamadoPor(String chamadoPor) {
    this.chamadoPor = chamadoPor;
  }

  public Long getUnidadeId() {
    return unidadeId;
  }

  public void setUnidadeId(Long unidadeId) {
    this.unidadeId = unidadeId;
  }

  public Long getUsuarioId() {
    return usuarioId;
  }

  public void setUsuarioId(Long usuarioId) {
    this.usuarioId = usuarioId;
  }
}
