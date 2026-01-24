package br.com.g3.biblioteca.dto;

import java.util.List;

public class BibliotecaEmprestimoListaResponse {
  private List<BibliotecaEmprestimoResponse> emprestimos;

  public BibliotecaEmprestimoListaResponse(List<BibliotecaEmprestimoResponse> emprestimos) {
    this.emprestimos = emprestimos;
  }

  public List<BibliotecaEmprestimoResponse> getEmprestimos() {
    return emprestimos;
  }

  public void setEmprestimos(List<BibliotecaEmprestimoResponse> emprestimos) {
    this.emprestimos = emprestimos;
  }
}
