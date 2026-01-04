package br.com.g3.transparencia.dto;

public class TransparenciaDestinacaoRequest {
  private Long id;
  private String titulo;
  private String descricao;
  private Integer percentual;

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

  public String getDescricao() {
    return descricao;
  }

  public void setDescricao(String descricao) {
    this.descricao = descricao;
  }

  public Integer getPercentual() {
    return percentual;
  }

  public void setPercentual(Integer percentual) {
    this.percentual = percentual;
  }
}
