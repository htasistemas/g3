package br.com.g3.auditoria.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.UUID;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "auditoria_evento")
public class AuditoriaEvento {
  @Id
  @Column(name = "id", columnDefinition = "uuid")
  private UUID id;

  @Column(name = "usuario_id")
  private Long usuarioId;

  @Column(name = "acao", length = 120, nullable = false)
  private String acao;

  @Column(name = "entidade", length = 120, nullable = false)
  private String entidade;

  @Column(name = "entidade_id", length = 120)
  private String entidadeId;

  @JdbcTypeCode(SqlTypes.JSON)
  @Column(name = "dados_json", columnDefinition = "jsonb")
  private String dadosJson;

  @Column(name = "criado_em", nullable = false)
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
