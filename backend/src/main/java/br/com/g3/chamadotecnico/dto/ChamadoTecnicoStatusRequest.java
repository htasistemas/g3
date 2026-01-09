package br.com.g3.chamadotecnico.dto;

import br.com.g3.chamadotecnico.domain.ChamadoStatus;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;

public class ChamadoTecnicoStatusRequest {
  @NotNull
  private ChamadoStatus status;

  @JsonProperty("usuario_id")
  private Long usuarioId;

  public ChamadoStatus getStatus() {
    return status;
  }

  public void setStatus(ChamadoStatus status) {
    this.status = status;
  }

  public Long getUsuarioId() {
    return usuarioId;
  }

  public void setUsuarioId(Long usuarioId) {
    this.usuarioId = usuarioId;
  }
}
