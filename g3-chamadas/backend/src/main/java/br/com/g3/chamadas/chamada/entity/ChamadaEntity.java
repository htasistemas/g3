package br.com.g3.chamadas.chamada.entity;

import br.com.g3.chamadas.fila.entity.FilaAtendimentoEntity;
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
@Table(name = "chamada")
public class ChamadaEntity {
  @Id
  @Column(name = "id_chamada", nullable = false)
  private UUID idChamada;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "id_fila_atendimento", nullable = false)
  private FilaAtendimentoEntity filaAtendimento;

  @Column(name = "nome_beneficiario", nullable = false, length = 200)
  private String nomeBeneficiario;

  @Column(name = "local_atendimento", nullable = false, length = 80)
  private String localAtendimento;

  @Column(name = "status_chamada", nullable = false, length = 20)
  private String statusChamada;

  @Column(name = "data_hora_chamada", nullable = false)
  private LocalDateTime dataHoraChamada;

  @Column(name = "chamado_por", length = 120)
  private String chamadoPor;

  @Column(name = "data_hora_criacao", nullable = false)
  private LocalDateTime dataHoraCriacao;

  public UUID getIdChamada() {
    return idChamada;
  }

  public void setIdChamada(UUID idChamada) {
    this.idChamada = idChamada;
  }

  public FilaAtendimentoEntity getFilaAtendimento() {
    return filaAtendimento;
  }

  public void setFilaAtendimento(FilaAtendimentoEntity filaAtendimento) {
    this.filaAtendimento = filaAtendimento;
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

  public String getStatusChamada() {
    return statusChamada;
  }

  public void setStatusChamada(String statusChamada) {
    this.statusChamada = statusChamada;
  }

  public LocalDateTime getDataHoraChamada() {
    return dataHoraChamada;
  }

  public void setDataHoraChamada(LocalDateTime dataHoraChamada) {
    this.dataHoraChamada = dataHoraChamada;
  }

  public String getChamadoPor() {
    return chamadoPor;
  }

  public void setChamadoPor(String chamadoPor) {
    this.chamadoPor = chamadoPor;
  }

  public LocalDateTime getDataHoraCriacao() {
    return dataHoraCriacao;
  }

  public void setDataHoraCriacao(LocalDateTime dataHoraCriacao) {
    this.dataHoraCriacao = dataHoraCriacao;
  }
}
