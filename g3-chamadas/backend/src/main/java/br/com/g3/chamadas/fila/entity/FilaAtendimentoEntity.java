package br.com.g3.chamadas.fila.entity;

import br.com.g3.chamadas.beneficiario.entity.BeneficiarioEntity;
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
@Table(name = "fila_atendimento")
public class FilaAtendimentoEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id_fila_atendimento")
  private Long idFilaAtendimento;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "id_beneficiario", nullable = false)
  private BeneficiarioEntity beneficiario;

  @Column(name = "status_fila", nullable = false, length = 20)
  private String statusFila;

  @Column(name = "prioridade", nullable = false)
  private Integer prioridade;

  @Column(name = "data_hora_entrada", nullable = false)
  private LocalDateTime dataHoraEntrada;

  @Column(name = "data_hora_atualizacao", nullable = false)
  private LocalDateTime dataHoraAtualizacao;

  public Long getIdFilaAtendimento() {
    return idFilaAtendimento;
  }

  public void setIdFilaAtendimento(Long idFilaAtendimento) {
    this.idFilaAtendimento = idFilaAtendimento;
  }

  public BeneficiarioEntity getBeneficiario() {
    return beneficiario;
  }

  public void setBeneficiario(BeneficiarioEntity beneficiario) {
    this.beneficiario = beneficiario;
  }

  public String getStatusFila() {
    return statusFila;
  }

  public void setStatusFila(String statusFila) {
    this.statusFila = statusFila;
  }

  public Integer getPrioridade() {
    return prioridade;
  }

  public void setPrioridade(Integer prioridade) {
    this.prioridade = prioridade;
  }

  public LocalDateTime getDataHoraEntrada() {
    return dataHoraEntrada;
  }

  public void setDataHoraEntrada(LocalDateTime dataHoraEntrada) {
    this.dataHoraEntrada = dataHoraEntrada;
  }

  public LocalDateTime getDataHoraAtualizacao() {
    return dataHoraAtualizacao;
  }

  public void setDataHoraAtualizacao(LocalDateTime dataHoraAtualizacao) {
    this.dataHoraAtualizacao = dataHoraAtualizacao;
  }
}
