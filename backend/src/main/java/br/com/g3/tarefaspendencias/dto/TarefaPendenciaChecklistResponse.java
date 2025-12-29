package br.com.g3.tarefaspendencias.dto;

import java.time.LocalDateTime;

public class TarefaPendenciaChecklistResponse {
  private Long id;
  private String titulo;
  private Boolean concluido;
  private LocalDateTime concluidoEm;
  private Integer ordem;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getTitulo() {
    return titulo;
  }

  public void setTitulo(String titulo) {
    this.titulo = titulo;
  }

  public Boolean getConcluido() {
    return concluido;
  }

  public void setConcluido(Boolean concluido) {
    this.concluido = concluido;
  }

  public LocalDateTime getConcluidoEm() {
    return concluidoEm;
  }

  public void setConcluidoEm(LocalDateTime concluidoEm) {
    this.concluidoEm = concluidoEm;
  }

  public Integer getOrdem() {
    return ordem;
  }

  public void setOrdem(Integer ordem) {
    this.ordem = ordem;
  }
}
