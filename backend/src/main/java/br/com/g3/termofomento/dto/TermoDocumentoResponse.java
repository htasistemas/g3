package br.com.g3.termofomento.dto;

public class TermoDocumentoResponse {
  private final Long id;
  private final String nome;
  private final String dataUrl;
  private final String tipo;

  public TermoDocumentoResponse(Long id, String nome, String dataUrl, String tipo) {
    this.id = id;
    this.nome = nome;
    this.dataUrl = dataUrl;
    this.tipo = tipo;
  }

  public Long getId() {
    return id;
  }

  public String getNome() {
    return nome;
  }

  public String getDataUrl() {
    return dataUrl;
  }

  public String getTipo() {
    return tipo;
  }
}
