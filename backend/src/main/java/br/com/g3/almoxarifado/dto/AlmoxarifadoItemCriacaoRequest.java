package br.com.g3.almoxarifado.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDate;

public class AlmoxarifadoItemCriacaoRequest {
  @NotBlank
  @Size(max = 30)
  @JsonProperty("codigo")
  private String codigo;

  @Size(max = 60)
  @JsonProperty("codigo_barras")
  private String codigoBarras;

  @NotBlank
  @Size(max = 200)
  @JsonProperty("descricao")
  private String descricao;

  @NotBlank
  @Size(max = 120)
  @JsonProperty("categoria")
  private String categoria;

  @NotBlank
  @Size(max = 60)
  @JsonProperty("unidade")
  private String unidade;

  @Size(max = 120)
  @JsonProperty("localizacao")
  private String localizacao;

  @Size(max = 120)
  @JsonProperty("localizacao_interna")
  private String localizacaoInterna;

  @NotNull
  @JsonProperty("estoque_atual")
  private Integer estoqueAtual;

  @NotNull
  @JsonProperty("estoque_minimo")
  private Integer estoqueMinimo;

  @NotNull
  @JsonProperty("valor_unitario")
  private BigDecimal valorUnitario;

  @NotBlank
  @Size(max = 20)
  @JsonProperty("situacao")
  private String situacao;

  @JsonFormat(pattern = "yyyy-MM-dd")
  @JsonProperty("validade")
  private LocalDate validade;

  @JsonProperty("ignorar_validade")
  private Boolean ignorarValidade;

  @JsonProperty("observacoes")
  private String observacoes;

  public String getCodigo() {
    return codigo;
  }

  public void setCodigo(String codigo) {
    this.codigo = codigo;
  }

  public String getCodigoBarras() {
    return codigoBarras;
  }

  public void setCodigoBarras(String codigoBarras) {
    this.codigoBarras = codigoBarras;
  }

  public String getDescricao() {
    return descricao;
  }

  public void setDescricao(String descricao) {
    this.descricao = descricao;
  }

  public String getCategoria() {
    return categoria;
  }

  public void setCategoria(String categoria) {
    this.categoria = categoria;
  }

  public String getUnidade() {
    return unidade;
  }

  public void setUnidade(String unidade) {
    this.unidade = unidade;
  }

  public String getLocalizacao() {
    return localizacao;
  }

  public void setLocalizacao(String localizacao) {
    this.localizacao = localizacao;
  }

  public String getLocalizacaoInterna() {
    return localizacaoInterna;
  }

  public void setLocalizacaoInterna(String localizacaoInterna) {
    this.localizacaoInterna = localizacaoInterna;
  }

  public Integer getEstoqueAtual() {
    return estoqueAtual;
  }

  public void setEstoqueAtual(Integer estoqueAtual) {
    this.estoqueAtual = estoqueAtual;
  }

  public Integer getEstoqueMinimo() {
    return estoqueMinimo;
  }

  public void setEstoqueMinimo(Integer estoqueMinimo) {
    this.estoqueMinimo = estoqueMinimo;
  }

  public BigDecimal getValorUnitario() {
    return valorUnitario;
  }

  public void setValorUnitario(BigDecimal valorUnitario) {
    this.valorUnitario = valorUnitario;
  }

  public String getSituacao() {
    return situacao;
  }

  public void setSituacao(String situacao) {
    this.situacao = situacao;
  }

  public LocalDate getValidade() {
    return validade;
  }

  public void setValidade(LocalDate validade) {
    this.validade = validade;
  }

  public Boolean getIgnorarValidade() {
    return ignorarValidade;
  }

  public void setIgnorarValidade(Boolean ignorarValidade) {
    this.ignorarValidade = ignorarValidade;
  }

  public String getObservacoes() {
    return observacoes;
  }

  public void setObservacoes(String observacoes) {
    this.observacoes = observacoes;
  }
}
