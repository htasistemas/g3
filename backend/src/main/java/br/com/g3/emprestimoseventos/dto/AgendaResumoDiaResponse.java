package br.com.g3.emprestimoseventos.dto;

import java.time.LocalDate;
import java.util.List;

public class AgendaResumoDiaResponse {
  private LocalDate data;
  private boolean temBloqueio;
  private int qtdEmprestimos;
  private List<Long> emprestimoIds;

  public LocalDate getData() {
    return data;
  }

  public void setData(LocalDate data) {
    this.data = data;
  }

  public boolean isTemBloqueio() {
    return temBloqueio;
  }

  public void setTemBloqueio(boolean temBloqueio) {
    this.temBloqueio = temBloqueio;
  }

  public int getQtdEmprestimos() {
    return qtdEmprestimos;
  }

  public void setQtdEmprestimos(int qtdEmprestimos) {
    this.qtdEmprestimos = qtdEmprestimos;
  }

  public List<Long> getEmprestimoIds() {
    return emprestimoIds;
  }

  public void setEmprestimoIds(List<Long> emprestimoIds) {
    this.emprestimoIds = emprestimoIds;
  }
}
