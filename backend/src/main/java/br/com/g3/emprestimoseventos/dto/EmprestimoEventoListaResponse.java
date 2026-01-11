package br.com.g3.emprestimoseventos.dto;

import java.util.List;

public class EmprestimoEventoListaResponse {
  private List<EmprestimoEventoResponse> emprestimos;

  public EmprestimoEventoListaResponse(List<EmprestimoEventoResponse> emprestimos) {
    this.emprestimos = emprestimos;
  }

  public List<EmprestimoEventoResponse> getEmprestimos() {
    return emprestimos;
  }

  public void setEmprestimos(List<EmprestimoEventoResponse> emprestimos) {
    this.emprestimos = emprestimos;
  }
}
