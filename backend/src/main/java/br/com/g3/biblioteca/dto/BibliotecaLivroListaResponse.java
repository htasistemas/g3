package br.com.g3.biblioteca.dto;

import java.util.List;

public class BibliotecaLivroListaResponse {
  private List<BibliotecaLivroResponse> livros;

  public BibliotecaLivroListaResponse(List<BibliotecaLivroResponse> livros) {
    this.livros = livros;
  }

  public List<BibliotecaLivroResponse> getLivros() {
    return livros;
  }

  public void setLivros(List<BibliotecaLivroResponse> livros) {
    this.livros = livros;
  }
}
