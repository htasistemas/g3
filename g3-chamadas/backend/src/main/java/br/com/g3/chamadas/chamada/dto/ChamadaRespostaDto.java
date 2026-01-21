package br.com.g3.chamadas.chamada.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public class ChamadaRespostaDto {
  private UUID idChamada;
  private Long idFilaAtendimento;
  private String nomeBeneficiario;
  private String localAtendimento;
  private String statusChamada;
  private LocalDateTime dataHoraChamada;
  private String chamadoPor;

  public ChamadaRespostaDto(
      UUID idChamada,
      Long idFilaAtendimento,
      String nomeBeneficiario,
      String localAtendimento,
      String statusChamada,
      LocalDateTime dataHoraChamada,
      String chamadoPor) {
    this.idChamada = idChamada;
    this.idFilaAtendimento = idFilaAtendimento;
    this.nomeBeneficiario = nomeBeneficiario;
    this.localAtendimento = localAtendimento;
    this.statusChamada = statusChamada;
    this.dataHoraChamada = dataHoraChamada;
    this.chamadoPor = chamadoPor;
  }

  public UUID getIdChamada() {
    return idChamada;
  }

  public Long getIdFilaAtendimento() {
    return idFilaAtendimento;
  }

  public String getNomeBeneficiario() {
    return nomeBeneficiario;
  }

  public String getLocalAtendimento() {
    return localAtendimento;
  }

  public String getStatusChamada() {
    return statusChamada;
  }

  public LocalDateTime getDataHoraChamada() {
    return dataHoraChamada;
  }

  public String getChamadoPor() {
    return chamadoPor;
  }
}
