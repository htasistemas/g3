package br.com.g3.informacoesadministrativas.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "g3_informacoes_administrativas")
public class InformacaoAdministrativa {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "tipo", length = 80, nullable = false)
  private String tipo;

  @Column(name = "categoria", length = 120, nullable = false)
  private String categoria;

  @Column(name = "titulo", length = 200, nullable = false)
  private String titulo;

  @Column(name = "descricao", columnDefinition = "text")
  private String descricao;

  @Column(name = "responsavel", length = 160)
  private String responsavel;

  @Column(name = "host_url", length = 300)
  private String hostUrl;

  @Column(name = "porta")
  private Integer porta;

  @Column(name = "login", length = 160)
  private String login;

  @Column(name = "segredo_ciphertext", columnDefinition = "text")
  private String segredoCiphertext;

  @Column(name = "segredo_iv", columnDefinition = "text")
  private String segredoIv;

  @Column(name = "segredo_tag", columnDefinition = "text")
  private String segredoTag;

  @Column(name = "tags", length = 300)
  private String tags;

  @Column(name = "status", nullable = false)
  private Boolean status;

  @Column(name = "criado_em", nullable = false)
  private LocalDateTime criadoEm;

  @Column(name = "criado_por", length = 120)
  private String criadoPor;

  @Column(name = "atualizado_em", nullable = false)
  private LocalDateTime atualizadoEm;

  @Column(name = "atualizado_por", length = 120)
  private String atualizadoPor;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getTipo() {
    return tipo;
  }

  public void setTipo(String tipo) {
    this.tipo = tipo;
  }

  public String getCategoria() {
    return categoria;
  }

  public void setCategoria(String categoria) {
    this.categoria = categoria;
  }

  public String getTitulo() {
    return titulo;
  }

  public void setTitulo(String titulo) {
    this.titulo = titulo;
  }

  public String getDescricao() {
    return descricao;
  }

  public void setDescricao(String descricao) {
    this.descricao = descricao;
  }

  public String getResponsavel() {
    return responsavel;
  }

  public void setResponsavel(String responsavel) {
    this.responsavel = responsavel;
  }

  public String getHostUrl() {
    return hostUrl;
  }

  public void setHostUrl(String hostUrl) {
    this.hostUrl = hostUrl;
  }

  public Integer getPorta() {
    return porta;
  }

  public void setPorta(Integer porta) {
    this.porta = porta;
  }

  public String getLogin() {
    return login;
  }

  public void setLogin(String login) {
    this.login = login;
  }

  public String getSegredoCiphertext() {
    return segredoCiphertext;
  }

  public void setSegredoCiphertext(String segredoCiphertext) {
    this.segredoCiphertext = segredoCiphertext;
  }

  public String getSegredoIv() {
    return segredoIv;
  }

  public void setSegredoIv(String segredoIv) {
    this.segredoIv = segredoIv;
  }

  public String getSegredoTag() {
    return segredoTag;
  }

  public void setSegredoTag(String segredoTag) {
    this.segredoTag = segredoTag;
  }

  public String getTags() {
    return tags;
  }

  public void setTags(String tags) {
    this.tags = tags;
  }

  public Boolean getStatus() {
    return status;
  }

  public void setStatus(Boolean status) {
    this.status = status;
  }

  public LocalDateTime getCriadoEm() {
    return criadoEm;
  }

  public void setCriadoEm(LocalDateTime criadoEm) {
    this.criadoEm = criadoEm;
  }

  public String getCriadoPor() {
    return criadoPor;
  }

  public void setCriadoPor(String criadoPor) {
    this.criadoPor = criadoPor;
  }

  public LocalDateTime getAtualizadoEm() {
    return atualizadoEm;
  }

  public void setAtualizadoEm(LocalDateTime atualizadoEm) {
    this.atualizadoEm = atualizadoEm;
  }

  public String getAtualizadoPor() {
    return atualizadoPor;
  }

  public void setAtualizadoPor(String atualizadoPor) {
    this.atualizadoPor = atualizadoPor;
  }
}
