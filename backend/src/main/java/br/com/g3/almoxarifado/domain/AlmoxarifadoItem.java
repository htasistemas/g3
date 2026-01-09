package br.com.g3.almoxarifado.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "almoxarifado_item")
public class AlmoxarifadoItem {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "codigo", length = 30, nullable = false, unique = true)
  private String codigo;

  @Column(name = "codigo_barras", length = 60, unique = true)
  private String codigoBarras;

  @Column(name = "descricao", length = 200, nullable = false)
  private String descricao;

  @Column(name = "categoria", length = 120, nullable = false)
  private String categoria;

  @Column(name = "unidade", length = 60, nullable = false)
  private String unidade;

  @Column(name = "localizacao", length = 120)
  private String localizacao;

  @Column(name = "localizacao_interna", length = 120)
  private String localizacaoInterna;

  @Column(name = "estoque_atual", nullable = false)
  private Integer estoqueAtual;

  @Column(name = "estoque_minimo", nullable = false)
  private Integer estoqueMinimo;

  @Column(name = "valor_unitario", nullable = false, precision = 12, scale = 2) 
  private BigDecimal valorUnitario;

  @Column(name = "is_kit", nullable = false)
  private Boolean isKit;

  @Column(name = "situacao", length = 20, nullable = false)
  private String situacao;

  @Column(name = "validade")
  private LocalDate validade;

  @Column(name = "ignorar_validade", nullable = false)
  private Boolean ignorarValidade;

  @Column(name = "observacoes")
  private String observacoes;

  @Column(name = "criado_em", nullable = false)
  private LocalDateTime criadoEm;

  @Column(name = "atualizado_em", nullable = false)
  private LocalDateTime atualizadoEm;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

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

  public Boolean getIsKit() {
    return isKit;
  }

  public void setIsKit(Boolean isKit) {
    this.isKit = isKit;
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

  public LocalDateTime getCriadoEm() {
    return criadoEm;
  }

  public void setCriadoEm(LocalDateTime criadoEm) {
    this.criadoEm = criadoEm;
  }

  public LocalDateTime getAtualizadoEm() {
    return atualizadoEm;
  }

  public void setAtualizadoEm(LocalDateTime atualizadoEm) {
    this.atualizadoEm = atualizadoEm;
  }
}
