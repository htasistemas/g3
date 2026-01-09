package br.com.g3.chamadotecnico.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public class ChamadoTecnicoAuditoriaVinculoRequest {
  @JsonProperty("auditoria_evento_id")
  @NotNull
  private UUID auditoriaEventoId;

  @JsonProperty("usuario_id")
  private Long usuarioId;

  public UUID getAuditoriaEventoId() {
    return auditoriaEventoId;
  }

  public void setAuditoriaEventoId(UUID auditoriaEventoId) {
    this.auditoriaEventoId = auditoriaEventoId;
  }

  public Long getUsuarioId() {
    return usuarioId;
  }

  public void setUsuarioId(Long usuarioId) {
    this.usuarioId = usuarioId;
  }
}
