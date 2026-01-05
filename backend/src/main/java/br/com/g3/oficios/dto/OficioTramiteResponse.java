package br.com.g3.oficios.dto;

import java.time.LocalDate;

public class OficioTramiteResponse {
  private Long id;
  private LocalDate data;
  private String origem;
  private String destino;
  private String responsavel;
  private String acao;
  private String observacoes;

  public OficioTramiteResponse(
      Long id,
      LocalDate data,
      String origem,
      String destino,
      String responsavel,
      String acao,
      String observacoes) {
    this.id = id;
    this.data = data;
    this.origem = origem;
    this.destino = destino;
    this.responsavel = responsavel;
    this.acao = acao;
    this.observacoes = observacoes;
  }

  public Long getId() {
    return id;
  }

  public LocalDate getData() {
    return data;
  }

  public String getOrigem() {
    return origem;
  }

  public String getDestino() {
    return destino;
  }

  public String getResponsavel() {
    return responsavel;
  }

  public String getAcao() {
    return acao;
  }

  public String getObservacoes() {
    return observacoes;
  }
}
