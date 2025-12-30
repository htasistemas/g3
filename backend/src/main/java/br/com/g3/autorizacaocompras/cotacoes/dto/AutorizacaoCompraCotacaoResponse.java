package br.com.g3.autorizacaocompras.cotacoes.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class AutorizacaoCompraCotacaoResponse {
  private Long id;
  private Long autorizacaoCompraId;
  private String fornecedor;
  private String razaoSocial;
  private String cnpj;
  private String cartaoCnpjUrl;
  private String cartaoCnpjNome;
  private String cartaoCnpjTipo;
  private String cartaoCnpjConteudo;
  private BigDecimal valor;
  private LocalDate prazoEntrega;
  private LocalDate validade;
  private String conformidade;
  private String observacoes;
  private String orcamentoFisicoNome;
  private String orcamentoFisicoTipo;
  private String orcamentoFisicoConteudo;
  private LocalDateTime criadoEm;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getAutorizacaoCompraId() {
    return autorizacaoCompraId;
  }

  public void setAutorizacaoCompraId(Long autorizacaoCompraId) {
    this.autorizacaoCompraId = autorizacaoCompraId;
  }

  public String getFornecedor() {
    return fornecedor;
  }

  public void setFornecedor(String fornecedor) {
    this.fornecedor = fornecedor;
  }

  public String getRazaoSocial() {
    return razaoSocial;
  }

  public void setRazaoSocial(String razaoSocial) {
    this.razaoSocial = razaoSocial;
  }

  public String getCnpj() {
    return cnpj;
  }

  public void setCnpj(String cnpj) {
    this.cnpj = cnpj;
  }

  public String getCartaoCnpjUrl() {
    return cartaoCnpjUrl;
  }

  public void setCartaoCnpjUrl(String cartaoCnpjUrl) {
    this.cartaoCnpjUrl = cartaoCnpjUrl;
  }

  public String getCartaoCnpjNome() {
    return cartaoCnpjNome;
  }

  public void setCartaoCnpjNome(String cartaoCnpjNome) {
    this.cartaoCnpjNome = cartaoCnpjNome;
  }

  public String getCartaoCnpjTipo() {
    return cartaoCnpjTipo;
  }

  public void setCartaoCnpjTipo(String cartaoCnpjTipo) {
    this.cartaoCnpjTipo = cartaoCnpjTipo;
  }

  public String getCartaoCnpjConteudo() {
    return cartaoCnpjConteudo;
  }

  public void setCartaoCnpjConteudo(String cartaoCnpjConteudo) {
    this.cartaoCnpjConteudo = cartaoCnpjConteudo;
  }

  public BigDecimal getValor() {
    return valor;
  }

  public void setValor(BigDecimal valor) {
    this.valor = valor;
  }

  public LocalDate getPrazoEntrega() {
    return prazoEntrega;
  }

  public void setPrazoEntrega(LocalDate prazoEntrega) {
    this.prazoEntrega = prazoEntrega;
  }

  public LocalDate getValidade() {
    return validade;
  }

  public void setValidade(LocalDate validade) {
    this.validade = validade;
  }

  public String getConformidade() {
    return conformidade;
  }

  public void setConformidade(String conformidade) {
    this.conformidade = conformidade;
  }

  public String getObservacoes() {
    return observacoes;
  }

  public void setObservacoes(String observacoes) {
    this.observacoes = observacoes;
  }

  public String getOrcamentoFisicoNome() {
    return orcamentoFisicoNome;
  }

  public void setOrcamentoFisicoNome(String orcamentoFisicoNome) {
    this.orcamentoFisicoNome = orcamentoFisicoNome;
  }

  public String getOrcamentoFisicoTipo() {
    return orcamentoFisicoTipo;
  }

  public void setOrcamentoFisicoTipo(String orcamentoFisicoTipo) {
    this.orcamentoFisicoTipo = orcamentoFisicoTipo;
  }

  public String getOrcamentoFisicoConteudo() {
    return orcamentoFisicoConteudo;
  }

  public void setOrcamentoFisicoConteudo(String orcamentoFisicoConteudo) {
    this.orcamentoFisicoConteudo = orcamentoFisicoConteudo;
  }

  public LocalDateTime getCriadoEm() {
    return criadoEm;
  }

  public void setCriadoEm(LocalDateTime criadoEm) {
    this.criadoEm = criadoEm;
  }
}
