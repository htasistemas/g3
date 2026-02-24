package br.com.g3.informacoesadministrativas.dto;

import java.time.LocalDateTime;

public class InformacaoAdministrativaResponse {
  private Long id;
  private String tipo;
  private String categoria;
  private String titulo;
  private String descricao;
  private String responsavel;
  private String hostUrl;
  private Integer porta;
  private String login;
  private String tags;
  private Boolean status;
  private LocalDateTime criadoEm;
  private String criadoPor;
  private LocalDateTime atualizadoEm;
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
