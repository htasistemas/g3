package br.com.g3.emprestimoseventos.repositoryimpl;

import java.time.LocalDateTime;

public class EmprestimoEventoAgendaRegistro {
  private Long emprestimoId;
  private LocalDateTime retiradaPrevista;
  private LocalDateTime devolucaoPrevista;
  private String status;

  public Long getEmprestimoId() {
    return emprestimoId;
  }

  public void setEmprestimoId(Long emprestimoId) {
    this.emprestimoId = emprestimoId;
  }

  public LocalDateTime getRetiradaPrevista() {
    return retiradaPrevista;
  }

  public void setRetiradaPrevista(LocalDateTime retiradaPrevista) {
    this.retiradaPrevista = retiradaPrevista;
  }

  public LocalDateTime getDevolucaoPrevista() {
    return devolucaoPrevista;
  }

  public void setDevolucaoPrevista(LocalDateTime devolucaoPrevista) {
    this.devolucaoPrevista = devolucaoPrevista;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }
}
