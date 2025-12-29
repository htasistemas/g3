package br.com.g3.almoxarifado.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDate;

public class AlmoxarifadoMovimentacaoResponse {
  @JsonProperty("id_movimentacao")
  private final Long id;

  @JsonFormat(pattern = "yyyy-MM-dd")
  @JsonProperty("data_movimentacao")
  private final LocalDate dataMovimentacao;

  @JsonProperty("tipo")
  private final String tipo;

  @JsonProperty("codigo_item")
  private final String codigoItem;

  @JsonProperty("descricao_item")
  private final String descricaoItem;

  @JsonProperty("quantidade")
  private final Integer quantidade;

  @JsonProperty("saldo_apos")
  private final Integer saldoApos;

  @JsonProperty("referencia")
  private final String referencia;

  @JsonProperty("responsavel")
  private final String responsavel;

  @JsonProperty("observacoes")
  private final String observacoes;

  public AlmoxarifadoMovimentacaoResponse(
      Long id,
      LocalDate dataMovimentacao,
      String tipo,
      String codigoItem,
      String descricaoItem,
      Integer quantidade,
      Integer saldoApos,
      String referencia,
      String responsavel,
      String observacoes) {
    this.id = id;
    this.dataMovimentacao = dataMovimentacao;
    this.tipo = tipo;
    this.codigoItem = codigoItem;
    this.descricaoItem = descricaoItem;
    this.quantidade = quantidade;
    this.saldoApos = saldoApos;
    this.referencia = referencia;
    this.responsavel = responsavel;
    this.observacoes = observacoes;
  }

  public Long getId() {
    return id;
  }

  public LocalDate getDataMovimentacao() {
    return dataMovimentacao;
  }

  public String getTipo() {
    return tipo;
  }

  public String getCodigoItem() {
    return codigoItem;
  }

  public String getDescricaoItem() {
    return descricaoItem;
  }

  public Integer getQuantidade() {
    return quantidade;
  }

  public Integer getSaldoApos() {
    return saldoApos;
  }

  public String getReferencia() {
    return referencia;
  }

  public String getResponsavel() {
    return responsavel;
  }

  public String getObservacoes() {
    return observacoes;
  }
}
