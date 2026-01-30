package br.com.g3.senhas.dto;

import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public class SenhaFinalizarRequest {
  @NotNull(message = "Chamada e obrigatoria.")
  private UUID chamadaId;

  public UUID getChamadaId() {
    return chamadaId;
  }

  public void setChamadaId(UUID chamadaId) {
    this.chamadaId = chamadaId;
  }
}
