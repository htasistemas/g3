package br.com.g3.almoxarifado.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import java.time.LocalDate;

public class AlmoxarifadoItemResponse {
  @JsonProperty("id_item")
  private final Long id;

  @JsonProperty("codigo")
  private final String codigo;

  @JsonProperty("codigo_barras")
  private final String codigoBarras;

  @JsonProperty("descricao")
  private final String descricao;

  @JsonProperty("categoria")
  private final String categoria;

  @JsonProperty("unidade")
  private final String unidade;

  @JsonProperty("localizacao")
  private final String localizacao;

  @JsonProperty("localizacao_interna")
  private final String localizacaoInterna;

  @JsonProperty("estoque_atual")
  private final Integer estoqueAtual;

  @JsonProperty("estoque_minimo")
  private final Integer estoqueMinimo;

  @JsonProperty("valor_unitario")
  private final BigDecimal valorUnitario;

  @JsonProperty("situacao")
  private final String situacao;

  @JsonFormat(pattern = "yyyy-MM-dd")
  @JsonProperty("validade")
  private final LocalDate validade;

  @JsonProperty("ignorar_validade")
  private final Boolean ignorarValidade;

  @JsonProperty("observacoes")
  private final String observacoes;

  public AlmoxarifadoItemResponse(
      Long id,
      String codigo,
      String codigoBarras,
      String descricao,
      String categoria,
      String unidade,
      String localizacao,
      String localizacaoInterna,
      Integer estoqueAtual,
      Integer estoqueMinimo,
      BigDecimal valorUnitario,
      String situacao,
      LocalDate validade,
      Boolean ignorarValidade,
      String observacoes) {
    this.id = id;
    this.codigo = codigo;
    this.codigoBarras = codigoBarras;
    this.descricao = descricao;
    this.categoria = categoria;
    this.unidade = unidade;
    this.localizacao = localizacao;
    this.localizacaoInterna = localizacaoInterna;
    this.estoqueAtual = estoqueAtual;
    this.estoqueMinimo = estoqueMinimo;
    this.valorUnitario = valorUnitario;
    this.situacao = situacao;
    this.validade = validade;
    this.ignorarValidade = ignorarValidade;
    this.observacoes = observacoes;
  }

  public Long getId() {
    return id;
  }

  public String getCodigo() {
    return codigo;
  }

  public String getCodigoBarras() {
    return codigoBarras;
  }

  public String getDescricao() {
    return descricao;
  }

  public String getCategoria() {
    return categoria;
  }

  public String getUnidade() {
    return unidade;
  }

  public String getLocalizacao() {
    return localizacao;
  }

  public String getLocalizacaoInterna() {
    return localizacaoInterna;
  }

  public Integer getEstoqueAtual() {
    return estoqueAtual;
  }

  public Integer getEstoqueMinimo() {
    return estoqueMinimo;
  }

  public BigDecimal getValorUnitario() {
    return valorUnitario;
  }

  public String getSituacao() {
    return situacao;
  }

  public LocalDate getValidade() {
    return validade;
  }

  public Boolean getIgnorarValidade() {
    return ignorarValidade;
  }

  public String getObservacoes() {
    return observacoes;
  }
}
