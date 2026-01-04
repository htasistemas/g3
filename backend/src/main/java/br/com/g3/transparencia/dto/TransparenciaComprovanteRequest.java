package br.com.g3.transparencia.dto;

public class TransparenciaComprovanteRequest {
  private Long id;
  private String titulo;
  private String descricao;
  private String arquivoNome;
  private String arquivoUrl;

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

  public String getArquivoNome() {
    return arquivoNome;
  }

  public void setArquivoNome(String arquivoNome) {
    this.arquivoNome = arquivoNome;
  }

  public String getArquivoUrl() {
    return arquivoUrl;
  }

  public void setArquivoUrl(String arquivoUrl) {
    this.arquivoUrl = arquivoUrl;
  }
}
