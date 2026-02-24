package br.com.g3.relatorios.dto;

public class InformacaoAdministrativaRelatorioRequest {
  private String tipo;
  private String categoria;
  private String titulo;
  private String tags;
  private Boolean status;
  private String usuarioEmissor;

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

  public String getUsuarioEmissor() {
    return usuarioEmissor;
  }

  public void setUsuarioEmissor(String usuarioEmissor) {
    this.usuarioEmissor = usuarioEmissor;
  }
}
