package br.com.g3.chamadotecnico.dto;

import br.com.g3.auditoria.dto.AuditoriaEventoResponse;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;
import java.util.UUID;

public class ChamadoTecnicoAuditoriaVinculoResponse {
  private UUID id;
  @JsonProperty("auditoria_evento")
  private AuditoriaEventoResponse auditoriaEvento;
  @JsonProperty("criado_em")
  private LocalDateTime criadoEm;

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public AuditoriaEventoResponse getAuditoriaEvento() {
    return auditoriaEvento;
  }

  public void setAuditoriaEvento(AuditoriaEventoResponse auditoriaEvento) {
    this.auditoriaEvento = auditoriaEvento;
  }

  public LocalDateTime getCriadoEm() {
    return criadoEm;
  }

  public void setCriadoEm(LocalDateTime criadoEm) {
    this.criadoEm = criadoEm;
  }
}
