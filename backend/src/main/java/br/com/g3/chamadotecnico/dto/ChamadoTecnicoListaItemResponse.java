package br.com.g3.chamadotecnico.dto;

import br.com.g3.chamadotecnico.domain.ChamadoPrioridade;
import br.com.g3.chamadotecnico.domain.ChamadoStatus;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;
import java.util.UUID;

public class ChamadoTecnicoListaItemResponse {
  private UUID id;
  private String codigo;
  private String titulo;
  private ChamadoStatus status;
  private ChamadoPrioridade prioridade;
  @JsonProperty("responsavel_usuario_id")
  private Long responsavelUsuarioId;
  private String modulo;
  private String cliente;
  @JsonProperty("criado_em")
  private LocalDateTime criadoEm;
  @JsonProperty("data_limite_sla")
  private LocalDateTime dataLimiteSla;
  @JsonProperty("sla_atrasado")
  private Boolean slaAtrasado;

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public String getCodigo() {
    return codigo;
  }

  public void setCodigo(String codigo) {
    this.codigo = codigo;
  }

  public String getTitulo() {
    return titulo;
  }

  public void setTitulo(String titulo) {
    this.titulo = titulo;
  }

  public ChamadoStatus getStatus() {
    return status;
  }

  public void setStatus(ChamadoStatus status) {
    this.status = status;
  }

  public ChamadoPrioridade getPrioridade() {
    return prioridade;
  }

  public void setPrioridade(ChamadoPrioridade prioridade) {
    this.prioridade = prioridade;
  }

  public Long getResponsavelUsuarioId() {
    return responsavelUsuarioId;
  }

  public void setResponsavelUsuarioId(Long responsavelUsuarioId) {
    this.responsavelUsuarioId = responsavelUsuarioId;
  }

  public String getModulo() {
    return modulo;
  }

  public void setModulo(String modulo) {
    this.modulo = modulo;
  }

  public String getCliente() {
    return cliente;
  }

  public void setCliente(String cliente) {
    this.cliente = cliente;
  }

  public LocalDateTime getCriadoEm() {
    return criadoEm;
  }

  public void setCriadoEm(LocalDateTime criadoEm) {
    this.criadoEm = criadoEm;
  }

  public LocalDateTime getDataLimiteSla() {
    return dataLimiteSla;
  }

  public void setDataLimiteSla(LocalDateTime dataLimiteSla) {
    this.dataLimiteSla = dataLimiteSla;
  }

  public Boolean getSlaAtrasado() {
    return slaAtrasado;
  }

  public void setSlaAtrasado(Boolean slaAtrasado) {
    this.slaAtrasado = slaAtrasado;
  }
}
