package br.com.g3.chamadotecnico.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;

public class ChamadoTecnicoAtribuicaoRequest {
  @JsonProperty("responsavel_usuario_id")
  private Long responsavelUsuarioId;

  @JsonProperty("usuario_id")
  @NotNull
  private Long usuarioId;

  public Long getResponsavelUsuarioId() {
    return responsavelUsuarioId;
  }

  public void setResponsavelUsuarioId(Long responsavelUsuarioId) {
    this.responsavelUsuarioId = responsavelUsuarioId;
  }

  public Long getUsuarioId() {
    return usuarioId;
  }

  public void setUsuarioId(Long usuarioId) {
    this.usuarioId = usuarioId;
  }
}
