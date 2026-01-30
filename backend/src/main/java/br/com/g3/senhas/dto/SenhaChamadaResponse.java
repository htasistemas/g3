package br.com.g3.senhas.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public class SenhaChamadaResponse {
  private UUID id;
  private Long filaId;
  private Long beneficiarioId;
  private String nomeBeneficiario;
  private String localAtendimento;
  private String status;
  private LocalDateTime dataHoraChamada;
  private Long unidadeId;
  private String chamadoPor;

  public SenhaChamadaResponse(
      UUID id,
      Long filaId,
      Long beneficiarioId,
      String nomeBeneficiario,
      String localAtendimento,
      String status,
      LocalDateTime dataHoraChamada,
      Long unidadeId,
      String chamadoPor) {
    this.id = id;
    this.filaId = filaId;
    this.beneficiarioId = beneficiarioId;
    this.nomeBeneficiario = nomeBeneficiario;
    this.localAtendimento = localAtendimento;
    this.status = status;
    this.dataHoraChamada = dataHoraChamada;
    this.unidadeId = unidadeId;
    this.chamadoPor = chamadoPor;
  }

  public UUID getId() {
    return id;
  }

  public Long getFilaId() {
    return filaId;
  }

  public Long getBeneficiarioId() {
    return beneficiarioId;
  }

  public String getNomeBeneficiario() {
    return nomeBeneficiario;
  }

  public String getLocalAtendimento() {
    return localAtendimento;
  }

  public String getStatus() {
    return status;
  }

  public LocalDateTime getDataHoraChamada() {
    return dataHoraChamada;
  }

  public Long getUnidadeId() {
    return unidadeId;
  }

  public String getChamadoPor() {
    return chamadoPor;
  }
}
