package br.com.g3.informacoesadministrativas.dto;

import jakarta.validation.constraints.NotBlank;

public class InformacaoAdministrativaRequest {
  @NotBlank
  private String tipo;

  @NotBlank
  private String categoria;

  @NotBlank
  private String titulo;

  private String descricao;
  private String responsavel;
  private String hostUrl;
  private Integer porta;
  private String login;
  private String segredo;
  private String tags;
  private Boolean status;
  private String usuarioResponsavel;

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

  public String getSegredo() {
    return segredo;
  }

  public void setSegredo(String segredo) {
    this.segredo = segredo;
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

  public String getUsuarioResponsavel() {
    return usuarioResponsavel;
  }

  public void setUsuarioResponsavel(String usuarioResponsavel) {
    this.usuarioResponsavel = usuarioResponsavel;
  }
}
