package br.com.g3.recebimentodoacao.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class RecebimentoDoacaoResponse {
  private Long id;
  private Long doadorId;
  private String doadorNome;
  private String tipoDoacao;
  private String descricao;
  private BigDecimal valor;
  private LocalDate dataRecebimento;
  private String formaRecebimento;
  private boolean recorrente;
  private String periodicidade;
  private LocalDate proximaCobranca;
  private String status;
  private String observacoes;
  private boolean contabilidadePendente;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getDoadorId() {
    return doadorId;
  }

  public void setDoadorId(Long doadorId) {
    this.doadorId = doadorId;
  }

  public String getDoadorNome() {
    return doadorNome;
  }

  public void setDoadorNome(String doadorNome) {
    this.doadorNome = doadorNome;
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
}
