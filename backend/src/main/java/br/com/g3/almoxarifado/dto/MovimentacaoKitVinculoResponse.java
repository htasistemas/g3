package br.com.g3.almoxarifado.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDate;

public class MovimentacaoKitVinculoResponse {
  @JsonProperty("movimentacao_id")
  private final Long movimentacaoId;

  @JsonProperty("data_movimentacao")
  @JsonFormat(pattern = "yyyy-MM-dd")
  private final LocalDate dataMovimentacao;

  @JsonProperty("tipo")
  private final String tipo;

  @JsonProperty("item_codigo")
  private final String itemCodigo;

  @JsonProperty("item_descricao")
  private final String itemDescricao;

  @JsonProperty("quantidade")
  private final Integer quantidade;

  @JsonProperty("saldo_apos")
  private final Integer saldoApos;

  public MovimentacaoKitVinculoResponse(
      Long movimentacaoId,
      LocalDate dataMovimentacao,
      String tipo,
      String itemCodigo,
      String itemDescricao,
      Integer quantidade,
      Integer saldoApos) {
    this.movimentacaoId = movimentacaoId;
    this.dataMovimentacao = dataMovimentacao;
    this.tipo = tipo;
    this.itemCodigo = itemCodigo;
    this.itemDescricao = itemDescricao;
    this.quantidade = quantidade;
    this.saldoApos = saldoApos;
  }

  public Long getMovimentacaoId() {
    return movimentacaoId;
  }

  public LocalDate getDataMovimentacao() {
    return dataMovimentacao;
  }

  public String getTipo() {
    return tipo;
  }

  public String getItemCodigo() {
    return itemCodigo;
  }

  public String getItemDescricao() {
    return itemDescricao;
  }

  public Integer getQuantidade() {
    return quantidade;
  }

  public Integer getSaldoApos() {
    return saldoApos;
  }
}
