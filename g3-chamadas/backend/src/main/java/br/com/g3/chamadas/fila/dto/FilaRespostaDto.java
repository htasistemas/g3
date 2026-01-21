package br.com.g3.chamadas.fila.dto;

import java.time.LocalDateTime;

public class FilaRespostaDto {
  private Long idFilaAtendimento;
  private Long idBeneficiario;
  private String nomeBeneficiario;
  private String statusFila;
  private Integer prioridade;
  private LocalDateTime dataHoraEntrada;

  public FilaRespostaDto(
      Long idFilaAtendimento,
      Long idBeneficiario,
      String nomeBeneficiario,
      String statusFila,
      Integer prioridade,
      LocalDateTime dataHoraEntrada) {
    this.idFilaAtendimento = idFilaAtendimento;
    this.idBeneficiario = idBeneficiario;
    this.nomeBeneficiario = nomeBeneficiario;
    this.statusFila = statusFila;
    this.prioridade = prioridade;
    this.dataHoraEntrada = dataHoraEntrada;
  }

  public Long getIdFilaAtendimento() {
    return idFilaAtendimento;
  }

  public Long getIdBeneficiario() {
    return idBeneficiario;
  }

  public String getNomeBeneficiario() {
    return nomeBeneficiario;
  }

  public String getStatusFila() {
    return statusFila;
  }

  public Integer getPrioridade() {
    return prioridade;
  }

  public LocalDateTime getDataHoraEntrada() {
    return dataHoraEntrada;
  }
}
