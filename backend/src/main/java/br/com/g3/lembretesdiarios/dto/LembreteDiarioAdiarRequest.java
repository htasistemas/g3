package br.com.g3.lembretesdiarios.dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public class LembreteDiarioAdiarRequest {
  @NotNull(message = "Nova data e hora sao obrigatorias")
  private LocalDateTime novaDataHora;

  public LocalDateTime getNovaDataHora() {
    return novaDataHora;
  }

  public void setNovaDataHora(LocalDateTime novaDataHora) {
    this.novaDataHora = novaDataHora;
  }
}
