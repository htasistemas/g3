package br.com.g3.oficios.dto;

import java.time.LocalDate;

public class OficioProtocoloResponse {
  private String status;
  private String protocoloEnvio;
  private LocalDate dataEnvio;
  private String protocoloRecebimento;
  private LocalDate dataRecebimento;
  private String proximoDestino;
  private String observacoes;

  public OficioProtocoloResponse(
      String status,
      String protocoloEnvio,
      LocalDate dataEnvio,
      String protocoloRecebimento,
      LocalDate dataRecebimento,
      String proximoDestino,
      String observacoes) {
    this.status = status;
    this.protocoloEnvio = protocoloEnvio;
    this.dataEnvio = dataEnvio;
    this.protocoloRecebimento = protocoloRecebimento;
    this.dataRecebimento = dataRecebimento;
    this.proximoDestino = proximoDestino;
    this.observacoes = observacoes;
  }

  public String getStatus() {
    return status;
  }

  public String getProtocoloEnvio() {
    return protocoloEnvio;
  }

  public LocalDate getDataEnvio() {
    return dataEnvio;
  }

  public String getProtocoloRecebimento() {
    return protocoloRecebimento;
  }

  public LocalDate getDataRecebimento() {
    return dataRecebimento;
  }

  public String getProximoDestino() {
    return proximoDestino;
  }

  public String getObservacoes() {
    return observacoes;
  }
}
