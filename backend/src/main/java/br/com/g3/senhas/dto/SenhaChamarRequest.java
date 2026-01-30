package br.com.g3.senhas.dto;

import jakarta.validation.constraints.NotNull;

public class SenhaChamarRequest {
  @NotNull(message = "Fila e obrigatoria.")
  private Long filaId;

  private String localAtendimento;

  private Long unidadeId;
  private Long usuarioId;

  public Long getFilaId() {
    return filaId;
  }

  public void setFilaId(Long filaId) {
    this.filaId = filaId;
  }

  public String getLocalAtendimento() {
    return localAtendimento;
  }

  public void setLocalAtendimento(String localAtendimento) {
    this.localAtendimento = localAtendimento;
  }

  public Long getUnidadeId() {
    return unidadeId;
  }

  public void setUnidadeId(Long unidadeId) {
    this.unidadeId = unidadeId;
  }

  public Long getUsuarioId() {
    return usuarioId;
  }

  public void setUsuarioId(Long usuarioId) {
    this.usuarioId = usuarioId;
  }
}
