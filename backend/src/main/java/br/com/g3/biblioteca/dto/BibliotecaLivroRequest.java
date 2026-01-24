package br.com.g3.biblioteca.dto;

public class BibliotecaLivroRequest {
  private String codigo;
  private String titulo;
  private String autor;
  private String isbn;
  private String editora;
  private Integer anoPublicacao;
  private String categoria;
  private Integer quantidadeTotal;
  private Integer quantidadeDisponivel;
  private String localizacao;
  private String status;
  private String estadoLivro;
  private String observacoes;

  public String getCodigo() {
    return codigo;
  }

  public void setCodigo(String codigo) {
    this.codigo = codigo;
  }

  public String getTitulo() {
    return titulo;
  }

  public void setTitulo(String titulo) {
    this.titulo = titulo;
  }

  public String getAutor() {
    return autor;
  }

  public void setAutor(String autor) {
    this.autor = autor;
  }

  public String getIsbn() {
    return isbn;
  }

  public void setIsbn(String isbn) {
    this.isbn = isbn;
  }

  public String getEditora() {
    return editora;
  }

  public void setEditora(String editora) {
    this.editora = editora;
  }

  public Integer getAnoPublicacao() {
    return anoPublicacao;
  }

  public void setAnoPublicacao(Integer anoPublicacao) {
    this.anoPublicacao = anoPublicacao;
  }

  public String getCategoria() {
    return categoria;
  }

  public void setCategoria(String categoria) {
    this.categoria = categoria;
  }

  public Integer getQuantidadeTotal() {
    return quantidadeTotal;
  }

  public void setQuantidadeTotal(Integer quantidadeTotal) {
    this.quantidadeTotal = quantidadeTotal;
  }

  public Integer getQuantidadeDisponivel() {
    return quantidadeDisponivel;
  }

  public void setQuantidadeDisponivel(Integer quantidadeDisponivel) {
    this.quantidadeDisponivel = quantidadeDisponivel;
  }

  public String getLocalizacao() {
    return localizacao;
  }

  public void setLocalizacao(String localizacao) {
    this.localizacao = localizacao;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getEstadoLivro() {
    return estadoLivro;
  }

  public void setEstadoLivro(String estadoLivro) {
    this.estadoLivro = estadoLivro;
  }

  public String getObservacoes() {
    return observacoes;
  }

  public void setObservacoes(String observacoes) {
    this.observacoes = observacoes;
  }
}
