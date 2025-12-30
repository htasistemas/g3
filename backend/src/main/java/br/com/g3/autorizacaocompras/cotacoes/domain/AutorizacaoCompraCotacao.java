package br.com.g3.autorizacaocompras.cotacoes.domain;

import br.com.g3.autorizacaocompras.domain.AutorizacaoCompra;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "autorizacao_compras_cotacoes")
public class AutorizacaoCompraCotacao {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "autorizacao_compra_id")
  private AutorizacaoCompra autorizacaoCompra;

  @Column(name = "fornecedor", length = 200, nullable = false)
  private String fornecedor;

  @Column(name = "razao_social", length = 200)
  private String razaoSocial;

  @Column(name = "cnpj", length = 20)
  private String cnpj;

  @Column(name = "cartao_cnpj_url", length = 400)
  private String cartaoCnpjUrl;

  @Column(name = "cartao_cnpj_nome", length = 255)
  private String cartaoCnpjNome;

  @Column(name = "cartao_cnpj_tipo", length = 100)
  private String cartaoCnpjTipo;

  @Column(name = "cartao_cnpj_conteudo")
  private String cartaoCnpjConteudo;

  @Column(name = "valor", precision = 14, scale = 2, nullable = false)
  private BigDecimal valor;

  @Column(name = "prazo_entrega")
  private LocalDate prazoEntrega;

  @Column(name = "validade")
  private LocalDate validade;

  @Column(name = "conformidade", length = 30)
  private String conformidade;

  @Column(name = "observacoes")
  private String observacoes;

  @Column(name = "orcamento_fisico_nome", length = 255)
  private String orcamentoFisicoNome;

  @Column(name = "orcamento_fisico_tipo", length = 100)
  private String orcamentoFisicoTipo;

  @Column(name = "orcamento_fisico_conteudo")
  private String orcamentoFisicoConteudo;

  @Column(name = "criado_em", nullable = false)
  private LocalDateTime criadoEm;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public AutorizacaoCompra getAutorizacaoCompra() {
    return autorizacaoCompra;
  }

  public void setAutorizacaoCompra(AutorizacaoCompra autorizacaoCompra) {
    this.autorizacaoCompra = autorizacaoCompra;
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
