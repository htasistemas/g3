package br.com.g3.autorizacaocompras.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class AutorizacaoCompraRequest {
  @NotBlank
  private String titulo;
  @NotBlank
  private String tipo;
  private String area;
  @NotBlank
  private String responsavel;
  private LocalDate dataPrevista;
  @NotNull
  private BigDecimal valor;
  private Integer quantidadeItens;
  private String justificativa;
  private String centroCusto;
  private String prioridade;
  @NotBlank
  private String status;
  private String aprovador;
  private String decisao;
  private String observacoesAprovacao;
  private LocalDate dataAprovacao;
  private Boolean dispensarCotacao;
  private String motivoDispensa;
  private String vencedor;
  private Boolean registroPatrimonio;
  private Boolean registroAlmoxarifado;
  private String numeroReserva;
  private String autorizacaoPagamentoNumero;
  private String autorizacaoPagamentoAutor;
  private LocalDate autorizacaoPagamentoData;
  private String autorizacaoPagamentoObservacoes;

  public String getTitulo() {
    return titulo;
  }

  public void setTitulo(String titulo) {
    this.titulo = titulo;
  }

  public String getTipo() {
    return tipo;
  }

  public void setTipo(String tipo) {
    this.tipo = tipo;
  }

  public String getArea() {
    return area;
  }

  public void setArea(String area) {
    this.area = area;
  }

  public String getResponsavel() {
    return responsavel;
  }

  public void setResponsavel(String responsavel) {
    this.responsavel = responsavel;
  }

  public LocalDate getDataPrevista() {
    return dataPrevista;
  }

  public void setDataPrevista(LocalDate dataPrevista) {
    this.dataPrevista = dataPrevista;
  }

  public BigDecimal getValor() {
    return valor;
  }

  public void setValor(BigDecimal valor) {
    this.valor = valor;
  }

  public Integer getQuantidadeItens() {
    return quantidadeItens;
  }

  public void setQuantidadeItens(Integer quantidadeItens) {
    this.quantidadeItens = quantidadeItens;
  }

  public String getPrioridade() {
    return prioridade;
  }

  public void setPrioridade(String prioridade) {
    this.prioridade = prioridade;
  }

  public String getJustificativa() {
    return justificativa;
  }

  public void setJustificativa(String justificativa) {
    this.justificativa = justificativa;
  }

  public String getCentroCusto() {
    return centroCusto;
  }

  public void setCentroCusto(String centroCusto) {
    this.centroCusto = centroCusto;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getAprovador() {
    return aprovador;
  }

  public void setAprovador(String aprovador) {
    this.aprovador = aprovador;
  }

  public String getDecisao() {
    return decisao;
  }

  public void setDecisao(String decisao) {
    this.decisao = decisao;
  }

  public String getObservacoesAprovacao() {
    return observacoesAprovacao;
  }

  public void setObservacoesAprovacao(String observacoesAprovacao) {
    this.observacoesAprovacao = observacoesAprovacao;
  }

  public LocalDate getDataAprovacao() {
    return dataAprovacao;
  }

  public void setDataAprovacao(LocalDate dataAprovacao) {
    this.dataAprovacao = dataAprovacao;
  }

  public Boolean getDispensarCotacao() {
    return dispensarCotacao;
  }

  public void setDispensarCotacao(Boolean dispensarCotacao) {
    this.dispensarCotacao = dispensarCotacao;
  }

  public String getMotivoDispensa() {
    return motivoDispensa;
  }

  public void setMotivoDispensa(String motivoDispensa) {
    this.motivoDispensa = motivoDispensa;
  }

  public String getVencedor() {
    return vencedor;
  }

  public void setVencedor(String vencedor) {
    this.vencedor = vencedor;
  }

  public Boolean getRegistroPatrimonio() {
    return registroPatrimonio;
  }

  public void setRegistroPatrimonio(Boolean registroPatrimonio) {
    this.registroPatrimonio = registroPatrimonio;
  }

  public Boolean getRegistroAlmoxarifado() {
    return registroAlmoxarifado;
  }

  public void setRegistroAlmoxarifado(Boolean registroAlmoxarifado) {
    this.registroAlmoxarifado = registroAlmoxarifado;
  }

  public String getNumeroReserva() {
    return numeroReserva;
  }

  public void setNumeroReserva(String numeroReserva) {
    this.numeroReserva = numeroReserva;
  }

  public String getAutorizacaoPagamentoNumero() {
    return autorizacaoPagamentoNumero;
  }

  public void setAutorizacaoPagamentoNumero(String autorizacaoPagamentoNumero) {
    this.autorizacaoPagamentoNumero = autorizacaoPagamentoNumero;
  }

  public String getAutorizacaoPagamentoAutor() {
    return autorizacaoPagamentoAutor;
  }

  public void setAutorizacaoPagamentoAutor(String autorizacaoPagamentoAutor) {
    this.autorizacaoPagamentoAutor = autorizacaoPagamentoAutor;
  }

  public LocalDate getAutorizacaoPagamentoData() {
    return autorizacaoPagamentoData;
  }

  public void setAutorizacaoPagamentoData(LocalDate autorizacaoPagamentoData) {
    this.autorizacaoPagamentoData = autorizacaoPagamentoData;
  }

  public String getAutorizacaoPagamentoObservacoes() {
    return autorizacaoPagamentoObservacoes;
  }

  public void setAutorizacaoPagamentoObservacoes(String autorizacaoPagamentoObservacoes) {
    this.autorizacaoPagamentoObservacoes = autorizacaoPagamentoObservacoes;
  }
}
