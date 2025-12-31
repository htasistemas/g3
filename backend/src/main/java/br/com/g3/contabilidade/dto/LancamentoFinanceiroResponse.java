package br.com.g3.contabilidade.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class LancamentoFinanceiroResponse {
  private Long id;
  private String tipo;
  private String descricao;
  private String contraparte;
  private LocalDate vencimento;
  private BigDecimal valor;
  private String situacao;
  private Long compraId;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getTipo() {
    return tipo;
  }

  public void setTipo(String tipo) {
    this.tipo = tipo;
  }

  public String getDescricao() {
    return descricao;
  }

  public void setDescricao(String descricao) {
    this.descricao = descricao;
  }

  public String getContraparte() {
    return contraparte;
  }

  public void setContraparte(String contraparte) {
    this.contraparte = contraparte;
  }

  public LocalDate getVencimento() {
    return vencimento;
  }

  public void setVencimento(LocalDate vencimento) {
    this.vencimento = vencimento;
  }

  public BigDecimal getValor() {
    return valor;
  }

  public void setValor(BigDecimal valor) {
    this.valor = valor;
  }

  public String getSituacao() {
    return situacao;
  }

  public void setSituacao(String situacao) {
    this.situacao = situacao;
  }

  public Long getCompraId() {
    return compraId;
  }

  public void setCompraId(Long compraId) {
    this.compraId = compraId;
  }
}
