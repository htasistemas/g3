package br.com.g3.contabilidade.domain;

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
@Table(name = "movimentacao_financeira")
public class MovimentacaoFinanceira {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "tipo", nullable = false, length = 20)
  private String tipo;

  @Column(name = "descricao", nullable = false, length = 200)
  private String descricao;

  @Column(name = "contraparte", length = 200)
  private String contraparte;

  @Column(name = "categoria", length = 80)
  private String categoria;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "conta_bancaria_id")
  private ContaBancaria contaBancaria;

  @Column(name = "data_movimentacao", nullable = false)
  private LocalDate dataMovimentacao;

  @Column(name = "valor", nullable = false, precision = 12, scale = 2)
  private BigDecimal valor;

  @Column(name = "criado_em", nullable = false)
  private LocalDateTime criadoEm;

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

  public String getCategoria() {
    return categoria;
  }

  public void setCategoria(String categoria) {
    this.categoria = categoria;
  }

  public ContaBancaria getContaBancaria() {
    return contaBancaria;
  }

  public void setContaBancaria(ContaBancaria contaBancaria) {
    this.contaBancaria = contaBancaria;
  }

  public LocalDate getDataMovimentacao() {
    return dataMovimentacao;
  }

  public void setDataMovimentacao(LocalDate dataMovimentacao) {
    this.dataMovimentacao = dataMovimentacao;
  }

  public BigDecimal getValor() {
    return valor;
  }

  public void setValor(BigDecimal valor) {
    this.valor = valor;
  }

  public LocalDateTime getCriadoEm() {
    return criadoEm;
  }

  public void setCriadoEm(LocalDateTime criadoEm) {
    this.criadoEm = criadoEm;
  }
}
