package br.com.g3.chamadotecnico.dto;

import br.com.g3.chamadotecnico.domain.ChamadoAcaoTipo;
import br.com.g3.chamadotecnico.domain.ChamadoStatus;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;
import java.util.UUID;

public class ChamadoTecnicoAcaoResponse {
  private UUID id;
  private ChamadoAcaoTipo tipo;
  private String descricao;
  @JsonProperty("de_status")
  private ChamadoStatus deStatus;
  @JsonProperty("para_status")
  private ChamadoStatus paraStatus;
  @JsonProperty("de_responsavel_id")
  private Long deResponsavelId;
  @JsonProperty("para_responsavel_id")
  private Long paraResponsavelId;
  @JsonProperty("criado_por_usuario_id")
  private Long criadoPorUsuarioId;
  @JsonProperty("criado_em")
  private LocalDateTime criadoEm;

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
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
