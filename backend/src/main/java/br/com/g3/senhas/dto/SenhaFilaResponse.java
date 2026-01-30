package br.com.g3.senhas.dto;

import java.time.LocalDateTime;

public class SenhaFilaResponse {
  private Long id;
  private Long beneficiarioId;
  private String nomeBeneficiario;
  private String status;
  private Integer prioridade;
  private LocalDateTime dataHoraEntrada;
  private Long unidadeId;
  private String salaAtendimento;

  public SenhaFilaResponse(
      Long id,
      Long beneficiarioId,
      String nomeBeneficiario,
      String status,
      Integer prioridade,
      LocalDateTime dataHoraEntrada,
      Long unidadeId,
      String salaAtendimento) {
    this.id = id;
    this.beneficiarioId = beneficiarioId;
    this.nomeBeneficiario = nomeBeneficiario;
    this.status = status;
    this.prioridade = prioridade;
    this.dataHoraEntrada = dataHoraEntrada;
    this.unidadeId = unidadeId;
    this.salaAtendimento = salaAtendimento;
  }

  public Long getId() {
    return id;
  }

  public Long getBeneficiarioId() {
    return beneficiarioId;
  }

  public String getNomeBeneficiario() {
    return nomeBeneficiario;
  }

  public String getStatus() {
    return status;
  }

  public Integer getPrioridade() {
    return prioridade;
  }

  public LocalDateTime getDataHoraEntrada() {
    return dataHoraEntrada;
  }

  public Long getUnidadeId() {
    return unidadeId;
  }

  public String getSalaAtendimento() {
    return salaAtendimento;
  }
}
