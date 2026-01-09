package br.com.g3.chamadotecnico.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.UUID;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "chamado_tecnico_acao")
public class ChamadoTecnicoAcao {
  @Id
  @Column(name = "id", columnDefinition = "uuid")
  private UUID id;

  @Column(name = "chamado_id", columnDefinition = "uuid", nullable = false)
  private UUID chamadoId;

  @Enumerated(EnumType.STRING)
  @JdbcTypeCode(SqlTypes.NAMED_ENUM)
  @Column(name = "tipo", columnDefinition = "chamado_acao_tipo", nullable = false)
  private ChamadoAcaoTipo tipo;

  @Column(name = "descricao", columnDefinition = "TEXT", nullable = false)
  private String descricao;

  @Enumerated(EnumType.STRING)
  @JdbcTypeCode(SqlTypes.NAMED_ENUM)
  @Column(name = "de_status", columnDefinition = "chamado_status")
  private ChamadoStatus deStatus;

  @Enumerated(EnumType.STRING)
  @JdbcTypeCode(SqlTypes.NAMED_ENUM)
  @Column(name = "para_status", columnDefinition = "chamado_status")
  private ChamadoStatus paraStatus;

  @Column(name = "de_responsavel_id")
  private Long deResponsavelId;

  @Column(name = "para_responsavel_id")
  private Long paraResponsavelId;

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

  public ChamadoAcaoTipo getTipo() {
    return tipo;
  }

  public void setTipo(ChamadoAcaoTipo tipo) {
    this.tipo = tipo;
  }

  public String getDescricao() {
    return descricao;
  }

  public void setDescricao(String descricao) {
    this.descricao = descricao;
  }

  public ChamadoStatus getDeStatus() {
    return deStatus;
  }

  public void setDeStatus(ChamadoStatus deStatus) {
    this.deStatus = deStatus;
  }

  public ChamadoStatus getParaStatus() {
    return paraStatus;
  }

  public void setParaStatus(ChamadoStatus paraStatus) {
    this.paraStatus = paraStatus;
  }

  public Long getDeResponsavelId() {
    return deResponsavelId;
  }

  public void setDeResponsavelId(Long deResponsavelId) {
    this.deResponsavelId = deResponsavelId;
  }

  public Long getParaResponsavelId() {
    return paraResponsavelId;
  }

  public void setParaResponsavelId(Long paraResponsavelId) {
    this.paraResponsavelId = paraResponsavelId;
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
