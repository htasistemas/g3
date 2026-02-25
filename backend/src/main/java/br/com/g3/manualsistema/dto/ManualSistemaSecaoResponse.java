package br.com.g3.manualsistema.dto;

import java.time.LocalDateTime;

public class ManualSistemaSecaoResponse {
  private String slug;
  private String titulo;
  private String conteudo;
  private Integer ordem;
  private String tags;
  private LocalDateTime atualizadoEm;
  private String atualizadoPor;
  private String versao;

  public String getSlug() {
    return slug;
  }

  public void setSlug(String slug) {
    this.slug = slug;
  }

  public String getTitulo() {
    return titulo;
  }

  public void setTitulo(String titulo) {
    this.titulo = titulo;
  }

  public String getConteudo() {
    return conteudo;
  }

  public void setConteudo(String conteudo) {
    this.conteudo = conteudo;
  }

  public Integer getOrdem() {
    return ordem;
  }

  public void setOrdem(Integer ordem) {
    this.ordem = ordem;
  }

  public String getTags() {
    return tags;
  }

  public void setTags(String tags) {
    this.tags = tags;
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

  public String getVersao() {
    return versao;
  }

  public void setVersao(String versao) {
    this.versao = versao;
  }
}
