package br.com.g3.patrimonio.dto;

import java.time.LocalDate;

public class PatrimonioMovimentoResponse {
  private Long idMovimento;
  private String tipo;
  private String destino;
  private String responsavel;
  private String observacao;
  private LocalDate dataMovimento;

  public Long getIdMovimento() {
    return idMovimento;
  }

  public void setIdMovimento(Long idMovimento) {
    this.idMovimento = idMovimento;
  }

  public String getTipo() {
    return tipo;
  }

  public void setTipo(String tipo) {
    this.tipo = tipo;
  }

  public String getDestino() {
    return destino;
  }

  public void setDestino(String destino) {
    this.destino = destino;
  }

  public String getResponsavel() {
    return responsavel;
  }

  public void setResponsavel(String responsavel) {
    this.responsavel = responsavel;
  }

  public String getObservacao() {
    return observacao;
  }

  public void setObservacao(String observacao) {
    this.observacao = observacao;
  }

  public LocalDate getDataMovimento() {
    return dataMovimento;
  }

  public void setDataMovimento(LocalDate dataMovimento) {
    this.dataMovimento = dataMovimento;
  }
}
