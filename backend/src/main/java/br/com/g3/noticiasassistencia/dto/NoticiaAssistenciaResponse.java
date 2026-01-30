package br.com.g3.noticiasassistencia.dto;

import java.time.LocalDateTime;

public class NoticiaAssistenciaResponse {
  private String titulo;
  private String link;
  private LocalDateTime publicadoEm;

  public NoticiaAssistenciaResponse() {}

  public NoticiaAssistenciaResponse(String titulo, String link, LocalDateTime publicadoEm) {
    this.titulo = titulo;
    this.link = link;
    this.publicadoEm = publicadoEm;
  }

  public String getTitulo() {
    return titulo;
  }

  public void setTitulo(String titulo) {
    this.titulo = titulo;
  }

  public String getLink() {
    return link;
  }

  public void setLink(String link) {
    this.link = link;
  }

  public LocalDateTime getPublicadoEm() {
    return publicadoEm;
  }

  public void setPublicadoEm(LocalDateTime publicadoEm) {
    this.publicadoEm = publicadoEm;
  }
}
