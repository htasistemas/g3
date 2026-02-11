package br.com.g3.recebimentodoacao.domain;

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
import java.time.LocalDateTime;

@Entity
@Table(name = "recebimento_doacao_item")
public class RecebimentoDoacaoItem {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "recebimento_doacao_id", nullable = false)
  private RecebimentoDoacao recebimentoDoacao;

  @Column(name = "descricao", nullable = false, length = 200)
  private String descricao;

  @Column(name = "quantidade", nullable = false)
  private Integer quantidade;

  @Column(name = "unidade", length = 20)
  private String unidade;

  @Column(name = "valor_unitario", precision = 12, scale = 2)
  private BigDecimal valorUnitario;

  @Column(name = "valor_total", precision = 12, scale = 2)
  private BigDecimal valorTotal;

  @Column(name = "marca", length = 120)
  private String marca;

  @Column(name = "modelo", length = 120)
  private String modelo;

  @Column(name = "conservacao", length = 80)
  private String conservacao;

  @Column(name = "observacoes")
  private String observacoes;

  @Column(name = "criado_em", nullable = false)
  private LocalDateTime criadoEm;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public RecebimentoDoacao getRecebimentoDoacao() {
    return recebimentoDoacao;
  }

  public void setRecebimentoDoacao(RecebimentoDoacao recebimentoDoacao) {
    this.recebimentoDoacao = recebimentoDoacao;
  }

  public String getDescricao() {
    return descricao;
  }

  public void setDescricao(String descricao) {
    this.descricao = descricao;
  }

  public Integer getQuantidade() {
    return quantidade;
  }

  public void setQuantidade(Integer quantidade) {
    this.quantidade = quantidade;
  }

  public String getUnidade() {
    return unidade;
  }

  public void setUnidade(String unidade) {
    this.unidade = unidade;
  }

  public BigDecimal getValorUnitario() {
    return valorUnitario;
  }

  public void setValorUnitario(BigDecimal valorUnitario) {
    this.valorUnitario = valorUnitario;
  }

  public BigDecimal getValorTotal() {
    return valorTotal;
  }

  public void setValorTotal(BigDecimal valorTotal) {
    this.valorTotal = valorTotal;
  }

  public String getMarca() {
    return marca;
  }

  public void setMarca(String marca) {
    this.marca = marca;
  }

  public String getModelo() {
    return modelo;
  }

  public void setModelo(String modelo) {
    this.modelo = modelo;
  }

  public String getConservacao() {
    return conservacao;
  }

  public void setConservacao(String conservacao) {
    this.conservacao = conservacao;
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
}
