package br.com.g3.patrimonio.dto;

import jakarta.validation.constraints.NotBlank;

public class PatrimonioMovimentoRequest {
  @NotBlank
  private String tipo;

  private String destino;
  private String responsavel;
  private String observacao;

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
}
