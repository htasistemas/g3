package br.com.g3.autorizacaocompras.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;

public class AutorizacaoPagamentoRequest {
  private String autor;

  @JsonFormat(pattern = "yyyy-MM-dd")
  private LocalDate data;

  private String observacoes;

  public String getAutor() {
    return autor;
  }

  public void setAutor(String autor) {
    this.autor = autor;
  }

  public LocalDate getData() {
    return data;
  }

  public void setData(LocalDate data) {
    this.data = data;
  }

  public String getObservacoes() {
    return observacoes;
  }

  public void setObservacoes(String observacoes) {
    this.observacoes = observacoes;
  }
}
