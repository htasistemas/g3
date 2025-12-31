package br.com.g3.contabilidade.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class ReciboPagamentoResponse {
  private String numeroRecibo;
  private LocalDate dataPagamento;
  private BigDecimal valorTotal;
  private Long compraId;
  private String descricao;
  private String responsavel;
  private List<ReciboPagamentoContaResponse> contas;

  public String getNumeroRecibo() {
    return numeroRecibo;
  }

  public void setNumeroRecibo(String numeroRecibo) {
    this.numeroRecibo = numeroRecibo;
  }

  public LocalDate getDataPagamento() {
    return dataPagamento;
  }

  public void setDataPagamento(LocalDate dataPagamento) {
    this.dataPagamento = dataPagamento;
  }

  public BigDecimal getValorTotal() {
    return valorTotal;
  }

  public void setValorTotal(BigDecimal valorTotal) {
    this.valorTotal = valorTotal;
  }

  public Long getCompraId() {
    return compraId;
  }

  public void setCompraId(Long compraId) {
    this.compraId = compraId;
  }

  public String getDescricao() {
    return descricao;
  }

  public void setDescricao(String descricao) {
    this.descricao = descricao;
  }

  public String getResponsavel() {
    return responsavel;
  }

  public void setResponsavel(String responsavel) {
    this.responsavel = responsavel;
  }

  public List<ReciboPagamentoContaResponse> getContas() {
    return contas;
  }

  public void setContas(List<ReciboPagamentoContaResponse> contas) {
    this.contas = contas;
  }
}
