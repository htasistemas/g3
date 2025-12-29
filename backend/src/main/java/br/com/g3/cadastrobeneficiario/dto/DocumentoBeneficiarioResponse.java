package br.com.g3.cadastrobeneficiario.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DocumentoBeneficiarioResponse {
  @JsonProperty("id")
  private final Long id;

  @JsonProperty("nome")
  private final String nome;

  @JsonProperty("nomeArquivo")
  private final String nomeArquivo;

  @JsonProperty("caminhoArquivo")
  private final String caminhoArquivo;

  @JsonProperty("contentType")
  private final String contentType;

  @JsonProperty("obrigatorio")
  private final Boolean obrigatorio;

  public DocumentoBeneficiarioResponse(
      Long id,
      String nome,
      String nomeArquivo,
      String caminhoArquivo,
      String contentType,
      Boolean obrigatorio) {
    this.id = id;
    this.nome = nome;
    this.nomeArquivo = nomeArquivo;
    this.caminhoArquivo = caminhoArquivo;
    this.contentType = contentType;
    this.obrigatorio = obrigatorio;
  }

  public Long getId() {
    return id;
  }

  public String getNome() {
    return nome;
  }

  public String getNomeArquivo() {
    return nomeArquivo;
  }

  public String getCaminhoArquivo() {
    return caminhoArquivo;
  }

  public String getContentType() {
    return contentType;
  }

  public Boolean getObrigatorio() {
    return obrigatorio;
  }
}
