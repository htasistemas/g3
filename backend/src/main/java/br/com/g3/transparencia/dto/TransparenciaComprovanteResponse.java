package br.com.g3.transparencia.dto;

public class TransparenciaComprovanteResponse {
  private final Long id;
  private final String titulo;
  private final String descricao;
  private final String arquivoNome;
  private final String arquivoUrl;

  public TransparenciaComprovanteResponse(
      Long id,
      String titulo,
      String descricao,
      String arquivoNome,
      String arquivoUrl) {
    this.id = id;
    this.titulo = titulo;
    this.descricao = descricao;
    this.arquivoNome = arquivoNome;
    this.arquivoUrl = arquivoUrl;
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

  public String getArquivoNome() {
    return arquivoNome;
  }

  public String getArquivoUrl() {
    return arquivoUrl;
  }
}
