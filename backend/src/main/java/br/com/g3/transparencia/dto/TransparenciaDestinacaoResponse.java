package br.com.g3.transparencia.dto;

public class TransparenciaDestinacaoResponse {
  private final Long id;
  private final String titulo;
  private final String descricao;
  private final Integer percentual;

  public TransparenciaDestinacaoResponse(
      Long id,
      String titulo,
      String descricao,
      Integer percentual) {
    this.id = id;
    this.titulo = titulo;
    this.descricao = descricao;
    this.percentual = percentual;
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

  public Integer getPercentual() {
    return percentual;
  }
}
