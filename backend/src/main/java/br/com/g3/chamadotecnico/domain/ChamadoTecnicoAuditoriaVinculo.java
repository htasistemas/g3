package br.com.g3.chamadotecnico.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "chamado_tecnico_auditoria_vinculo")
public class ChamadoTecnicoAuditoriaVinculo {
  @Id
  @Column(name = "id", columnDefinition = "uuid")
  private UUID id;

  @Column(name = "chamado_id", columnDefinition = "uuid", nullable = false)
  private UUID chamadoId;

  @Column(name = "auditoria_evento_id", columnDefinition = "uuid", nullable = false)
  private UUID auditoriaEventoId;

  @Column(name = "criado_por_usuario_id")
  private Long criadoPorUsuarioId;

  @Column(name = "criado_em", nullable = false)
  private LocalDateTime criadoEm;

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public UUID getChamadoId() {
    return chamadoId;
  }

  public void setChamadoId(UUID chamadoId) {
    this.chamadoId = chamadoId;
  }

  public UUID getAuditoriaEventoId() {
    return auditoriaEventoId;
  }

  public void setAuditoriaEventoId(UUID auditoriaEventoId) {
    this.auditoriaEventoId = auditoriaEventoId;
  }

  public Long getCriadoPorUsuarioId() {
    return criadoPorUsuarioId;
  }

  public void setCriadoPorUsuarioId(Long criadoPorUsuarioId) {
    this.criadoPorUsuarioId = criadoPorUsuarioId;
  }

  public LocalDateTime getCriadoEm() {
    return criadoEm;
  }

  public void setCriadoEm(LocalDateTime criadoEm) {
    this.criadoEm = criadoEm;
  }
}
