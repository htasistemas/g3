package br.com.g3.controleveiculos.dto;

public class MotoristaDisponivelResponse {
  private final Long id;
  private final String tipoOrigem;
  private final String nome;

  public MotoristaDisponivelResponse(Long id, String tipoOrigem, String nome) {
    this.id = id;
    this.tipoOrigem = tipoOrigem;
    this.nome = nome;
  }

  public Long getId() {
    return id;
  }

  public String getTipoOrigem() {
    return tipoOrigem;
  }

  public String getNome() {
    return nome;
  }
}
