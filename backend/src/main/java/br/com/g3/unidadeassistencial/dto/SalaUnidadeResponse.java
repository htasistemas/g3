package br.com.g3.unidadeassistencial.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SalaUnidadeResponse {
  private final Long id;

  @JsonProperty("unidade_id")
  private final Long unidadeId;

  private final String nome;

  public SalaUnidadeResponse(Long id, Long unidadeId, String nome) {
    this.id = id;
    this.unidadeId = unidadeId;
    this.nome = nome;
  }

  public Long getId() {
    return id;
  }

  public Long getUnidadeId() {
    return unidadeId;
  }

  public String getNome() {
    return nome;
  }
}
