package br.com.g3.auditoria.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;
import java.util.UUID;

public class AuditoriaEventoResponse {
  private UUID id;
  @JsonProperty("usuario_id")
  private Long usuarioId;
  private String acao;
  private String entidade;
  @JsonProperty("entidade_id")
  private String entidadeId;
  @JsonProperty("dados_json")
  private String dadosJson;
  @JsonProperty("criado_em")
  private LocalDateTime criadoEm;

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public Long getUsuarioId() {
    return usuarioId;
  }

  public void setUsuarioId(Long usuarioId) {
    this.usuarioId = usuarioId;
  }

  public String getAcao() {
    return acao;
  }

  public void setAcao(String acao) {
    this.acao = acao;
  }

  public String getEntidade() {
    return entidade;
  }

  public void setEntidade(String entidade) {
    this.entidade = entidade;
  }

  public String getEntidadeId() {
    return entidadeId;
  }

  public void setEntidadeId(String entidadeId) {
    this.entidadeId = entidadeId;
  }

  public String getDadosJson() {
    return dadosJson;
  }

  public void setDadosJson(String dadosJson) {
    this.dadosJson = dadosJson;
  }

  public LocalDateTime getCriadoEm() {
    return criadoEm;
  }

  public void setCriadoEm(LocalDateTime criadoEm) {
    this.criadoEm = criadoEm;
  }
}
