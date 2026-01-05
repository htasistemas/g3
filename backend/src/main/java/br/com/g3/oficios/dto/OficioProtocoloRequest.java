package br.com.g3.oficios.dto;

import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate;

public class OficioProtocoloRequest {
  @NotBlank
  private String status;

  private String protocoloEnvio;
  private LocalDate dataEnvio;
  private String protocoloRecebimento;
  private LocalDate dataRecebimento;
  private String proximoDestino;
  private String observacoes;

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getProtocoloEnvio() {
    return protocoloEnvio;
  }

  public void setProtocoloEnvio(String protocoloEnvio) {
    this.protocoloEnvio = protocoloEnvio;
  }

  public LocalDate getDataEnvio() {
    return dataEnvio;
  }

  public void setDataEnvio(LocalDate dataEnvio) {
    this.dataEnvio = dataEnvio;
  }

  public String getProtocoloRecebimento() {
    return protocoloRecebimento;
  }

  public void setProtocoloRecebimento(String protocoloRecebimento) {
    this.protocoloRecebimento = protocoloRecebimento;
  }

  public LocalDate getDataRecebimento() {
    return dataRecebimento;
  }

  public void setDataRecebimento(LocalDate dataRecebimento) {
    this.dataRecebimento = dataRecebimento;
  }

  public String getProximoDestino() {
    return proximoDestino;
  }

  public void setProximoDestino(String proximoDestino) {
    this.proximoDestino = proximoDestino;
  }

  public String getObservacoes() {
    return observacoes;
  }

  public void setObservacoes(String observacoes) {
    this.observacoes = observacoes;
  }
}
