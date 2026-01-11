package br.com.g3.emprestimoseventos.dto;

import java.time.LocalDateTime;

public class ConflitoEmprestimoItemResponse {
  private Long emprestimoId;
  private String eventoTitulo;
  private LocalDateTime inicio;
  private LocalDateTime fim;
  private String status;
  private Integer quantidadeReservada;

  public Long getEmprestimoId() {
    return emprestimoId;
  }

  public void setEmprestimoId(Long emprestimoId) {
    this.emprestimoId = emprestimoId;
  }

  public String getEventoTitulo() {
    return eventoTitulo;
  }

  public void setEventoTitulo(String eventoTitulo) {
    this.eventoTitulo = eventoTitulo;
  }

  public LocalDateTime getInicio() {
    return inicio;
  }

  public void setInicio(LocalDateTime inicio) {
    this.inicio = inicio;
  }

  public LocalDateTime getFim() {
    return fim;
  }

  public void setFim(LocalDateTime fim) {
    this.fim = fim;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public Integer getQuantidadeReservada() {
    return quantidadeReservada;
  }

  public void setQuantidadeReservada(Integer quantidadeReservada) {
    this.quantidadeReservada = quantidadeReservada;
  }
}
