package br.com.g3.emprestimoseventos.dto;

import java.util.List;

public class DisponibilidadeItemResponse {
  private boolean disponivel;
  private Integer quantidadeDisponivel;
  private List<ConflitoEmprestimoItemResponse> conflitos;

  public boolean isDisponivel() {
    return disponivel;
  }

  public void setDisponivel(boolean disponivel) {
    this.disponivel = disponivel;
  }

  public Integer getQuantidadeDisponivel() {
    return quantidadeDisponivel;
  }

  public void setQuantidadeDisponivel(Integer quantidadeDisponivel) {
    this.quantidadeDisponivel = quantidadeDisponivel;
  }

  public List<ConflitoEmprestimoItemResponse> getConflitos() {
    return conflitos;
  }

  public void setConflitos(List<ConflitoEmprestimoItemResponse> conflitos) {
    this.conflitos = conflitos;
  }
}
