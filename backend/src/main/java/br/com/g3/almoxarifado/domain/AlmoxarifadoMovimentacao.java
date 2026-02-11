package br.com.g3.almoxarifado.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "almoxarifado_movimentacao")
public class AlmoxarifadoMovimentacao {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "item_id")
  private AlmoxarifadoItem item;

  @Column(name = "data_movimentacao", nullable = false)
  private LocalDate dataMovimentacao;

  @Column(name = "tipo", length = 20, nullable = false)
  private String tipo;

  @Column(name = "quantidade", nullable = false)
  private Integer quantidade;

  @Column(name = "saldo_apos", nullable = false)
  private Integer saldoApos;

  @Column(name = "referencia", length = 150)
  private String referencia;

  @Column(name = "responsavel", length = 150)
  private String responsavel;

  @Column(name = "observacoes")
  private String observacoes;

  @Column(name = "direcao_ajuste", length = 20)
  private String direcaoAjuste;

  @Column(name = "doacao_id")
  private Long doacaoId;

  @Column(name = "criado_em", nullable = false)
  private LocalDateTime criadoEm;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public AlmoxarifadoItem getItem() {
    return item;
  }

  public void setItem(AlmoxarifadoItem item) {
    this.item = item;
  }

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

  public Integer getQuantidade() {
    return quantidade;
  }

  public void setQuantidade(Integer quantidade) {
    this.quantidade = quantidade;
  }

  public Integer getSaldoApos() {
    return saldoApos;
  }

  public void setSaldoApos(Integer saldoApos) {
    this.saldoApos = saldoApos;
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

  public String getDirecaoAjuste() {
    return direcaoAjuste;
  }

  public void setDirecaoAjuste(String direcaoAjuste) {
    this.direcaoAjuste = direcaoAjuste;
  }

  public Long getDoacaoId() {
    return doacaoId;
  }

  public void setDoacaoId(Long doacaoId) {
    this.doacaoId = doacaoId;
  }

  public LocalDateTime getCriadoEm() {
    return criadoEm;
  }

  public void setCriadoEm(LocalDateTime criadoEm) {
    this.criadoEm = criadoEm;
  }
}
