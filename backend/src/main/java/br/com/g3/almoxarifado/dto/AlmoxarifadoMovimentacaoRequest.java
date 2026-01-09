package br.com.g3.almoxarifado.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

public class AlmoxarifadoMovimentacaoRequest {
  @NotNull
  @JsonFormat(pattern = "yyyy-MM-dd")
  @JsonProperty("data_movimentacao")
  private LocalDate dataMovimentacao;

  @NotBlank
  @Size(max = 20)
  @JsonProperty("tipo")
  private String tipo;

  @NotBlank
  @Size(max = 30)
  @JsonProperty("codigo_item")
  private String codigoItem;

  @NotNull
  @JsonProperty("quantidade")
  private Integer quantidade;

  @Size(max = 150)
  @JsonProperty("referencia")
  private String referencia;

  @Size(max = 150)
  @JsonProperty("responsavel")
  private String responsavel;

  @JsonProperty("observacoes")
  private String observacoes;

  @JsonProperty("gerar_itens_kit")
  private Boolean gerarItensKit;

  @Size(max = 20)
  @JsonProperty("direcao_ajuste")
  private String direcaoAjuste;

  public LocalDate getDataMovimentacao() {
    return dataMovimentacao;
  }

  public void setDataMovimentacao(LocalDate dataMovimentacao) {
    this.dataMovimentacao = dataMovimentacao;
  }

  public String getTipo() {
    return tipo;
  }

  public void setTipo(String tipo) {
    this.tipo = tipo;
  }

  public String getCodigoItem() {
    return codigoItem;
  }

  public void setCodigoItem(String codigoItem) {
    this.codigoItem = codigoItem;
  }

  public Integer getQuantidade() {
    return quantidade;
  }

  public void setQuantidade(Integer quantidade) {
    this.quantidade = quantidade;
  }

  public String getReferencia() {
    return referencia;
  }

  public void setReferencia(String referencia) {
    this.referencia = referencia;
  }

  public String getResponsavel() {
    return responsavel;
  }

  public void setResponsavel(String responsavel) {
    this.responsavel = responsavel;
  }

  public String getObservacoes() {
    return observacoes;
  }

  public void setObservacoes(String observacoes) {
    this.observacoes = observacoes;
  }

  public Boolean getGerarItensKit() {
    return gerarItensKit;
  }

  public void setGerarItensKit(Boolean gerarItensKit) {
    this.gerarItensKit = gerarItensKit;
  }

  public String getDirecaoAjuste() {
    return direcaoAjuste;
  }

  public void setDirecaoAjuste(String direcaoAjuste) {
    this.direcaoAjuste = direcaoAjuste;
  }
}
