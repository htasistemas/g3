package br.com.g3.transparencia.dto;

public class TransparenciaTimelineResponse {
  private final Long id;
  private final String titulo;
  private final String detalhe;
  private final String status;

  public TransparenciaTimelineResponse(Long id, String titulo, String detalhe, String status) {
    this.id = id;
    this.titulo = titulo;
    this.detalhe = detalhe;
    this.status = status;
  }

  public Long getId() {
    return id;
  }

  public String getTitulo() {
    return titulo;
  }

  public String getDetalhe() {
    return detalhe;
  }

  public String getStatus() {
    return status;
  }
}
