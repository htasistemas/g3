package br.com.g3.manualsistema.dto;

import java.time.LocalDateTime;

public class ManualSistemaSecaoResumoResponse {
  private String slug;
  private String titulo;
  private Integer ordem;
  private String tags;
  private LocalDateTime atualizadoEm;

  public ManualSistemaSecaoResumoResponse() {}

  public ManualSistemaSecaoResumoResponse(
      String slug, String titulo, Integer ordem, String tags, LocalDateTime atualizadoEm) {
    this.slug = slug;
    this.titulo = titulo;
    this.ordem = ordem;
    this.tags = tags;
    this.atualizadoEm = atualizadoEm;
  }

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
}
