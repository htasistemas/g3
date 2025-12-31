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
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "recebimento_doacao")
public class RecebimentoDoacao {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "doador_id")
  private Doador doador;

  @Column(name = "tipo_doacao", length = 40, nullable = false)
  private String tipoDoacao;

  @Column(name = "descricao")
  private String descricao;

  @Column(name = "quantidade_itens")
  private Integer quantidadeItens;

  @Column(name = "valor_medio", precision = 12, scale = 2)
  private BigDecimal valorMedio;

  @Column(name = "valor_total", precision = 12, scale = 2)
  private BigDecimal valorTotal;

  @Column(name = "valor", precision = 12, scale = 2)
  private BigDecimal valor;

  @Column(name = "data_recebimento", nullable = false)
  private LocalDate dataRecebimento;

  @Column(name = "forma_recebimento", length = 60)
  private String formaRecebimento;

  @Column(name = "recorrente", nullable = false)
  private boolean recorrente;

  @Column(name = "periodicidade", length = 40)
  private String periodicidade;

  @Column(name = "proxima_cobranca")
  private LocalDate proximaCobranca;

  @Column(name = "status", length = 40, nullable = false)
  private String status;

  @Column(name = "observacoes")
  private String observacoes;

  @Column(name = "contabilidade_pendente", nullable = false)
  private boolean contabilidadePendente;

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

  public Doador getDoador() {
    return doador;
  }

  public void setDoador(Doador doador) {
    this.doador = doador;
  }

  public String getTipoDoacao() {
    return tipoDoacao;
  }

  public void setTipoDoacao(String tipoDoacao) {
    this.tipoDoacao = tipoDoacao;
  }

  public String getDescricao() {
    return descricao;
  }

  public void setDescricao(String descricao) {
    this.descricao = descricao;
  }

  public Integer getQuantidadeItens() {
    return quantidadeItens;
  }

  public void setQuantidadeItens(Integer quantidadeItens) {
    this.quantidadeItens = quantidadeItens;
  }

  public BigDecimal getValorMedio() {
    return valorMedio;
  }

  public void setValorMedio(BigDecimal valorMedio) {
    this.valorMedio = valorMedio;
  }

  public BigDecimal getValorTotal() {
    return valorTotal;
  }

  public void setValorTotal(BigDecimal valorTotal) {
    this.valorTotal = valorTotal;
  }

  public BigDecimal getValor() {
    return valor;
  }

  public void setValor(BigDecimal valor) {
    this.valor = valor;
  }

  public LocalDate getDataRecebimento() {
    return dataRecebimento;
  }

  public void setDataRecebimento(LocalDate dataRecebimento) {
    this.dataRecebimento = dataRecebimento;
  }

  public String getFormaRecebimento() {
    return formaRecebimento;
  }

  public void setFormaRecebimento(String formaRecebimento) {
    this.formaRecebimento = formaRecebimento;
  }

  public boolean isRecorrente() {
    return recorrente;
  }

  public void setRecorrente(boolean recorrente) {
    this.recorrente = recorrente;
  }

  public String getPeriodicidade() {
    return periodicidade;
  }

  public void setPeriodicidade(String periodicidade) {
    this.periodicidade = periodicidade;
  }

  public LocalDate getProximaCobranca() {
    return proximaCobranca;
  }

  public void setProximaCobranca(LocalDate proximaCobranca) {
    this.proximaCobranca = proximaCobranca;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getObservacoes() {
    return observacoes;
  }

  public void setObservacoes(String observacoes) {
    this.observacoes = observacoes;
  }

  public boolean isContabilidadePendente() {
    return contabilidadePendente;
  }

  public void setContabilidadePendente(boolean contabilidadePendente) {
    this.contabilidadePendente = contabilidadePendente;
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
