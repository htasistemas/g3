package br.com.g3.transparencia.dto;

public class TransparenciaChecklistResponse {
  private final Long id;
  private final String titulo;
  private final String descricao;
  private final String status;

  public TransparenciaChecklistResponse(
      Long id,
      String titulo,
      String descricao,
      String status) {
    this.id = id;
    this.titulo = titulo;
    this.descricao = descricao;
    this.status = status;
  }

  public Long getId() {
    return id;
  }

  public String getTitulo() {
    return titulo;
  }

  public String getDescricao() {
    return descricao;
  }

  public String getStatus() {
    return status;
  }
}
