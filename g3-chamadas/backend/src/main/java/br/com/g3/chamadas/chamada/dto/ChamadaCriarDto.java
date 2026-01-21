package br.com.g3.chamadas.chamada.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class ChamadaCriarDto {
  @NotNull(message = "Fila obrigatoria.")
  private Long idFilaAtendimento;

  @NotBlank(message = "Local de atendimento obrigatorio.")
  private String localAtendimento;

  public Long getIdFilaAtendimento() {
    return idFilaAtendimento;
  }

  public void setIdFilaAtendimento(Long idFilaAtendimento) {
    this.idFilaAtendimento = idFilaAtendimento;
  }

  public String getLocalAtendimento() {
    return localAtendimento;
  }

  public void setLocalAtendimento(String localAtendimento) {
    this.localAtendimento = localAtendimento;
  }
}
