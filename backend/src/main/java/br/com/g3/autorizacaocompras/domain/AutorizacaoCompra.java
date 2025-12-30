package br.com.g3.autorizacaocompras.domain;

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
@Table(name = "autorizacao_compras")
public class AutorizacaoCompra {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "titulo", length = 255, nullable = false)
  private String titulo;

  @Column(name = "tipo", length = 30, nullable = false)
  private String tipo;

  @Column(name = "area", length = 150)
  private String area;

  @Column(name = "responsavel", length = 150)
  private String responsavel;

  @Column(name = "data_prevista")
  private LocalDate dataPrevista;

  @Column(name = "valor", precision = 14, scale = 2)
  private BigDecimal valor;

  @Column(name = "quantidade_itens", nullable = false)
  private Integer quantidadeItens = 1;

  @Column(name = "justificativa")
  private String justificativa;

  @Column(name = "centro_custo", length = 60)
  private String centroCusto;

  @Column(name = "prioridade", length = 20, nullable = false)
  private String prioridade = "normal";

  @Column(name = "status", length = 40, nullable = false)
  private String status;

  @Column(name = "aprovador", length = 150)
  private String aprovador;

  @Column(name = "decisao", length = 30)
  private String decisao;

  @Column(name = "observacoes_aprovacao")
  private String observacoesAprovacao;

  @Column(name = "data_aprovacao")
  private LocalDate dataAprovacao;

  @Column(name = "dispensar_cotacao", nullable = false)
  private Boolean dispensarCotacao = false;

  @Column(name = "motivo_dispensa")
  private String motivoDispensa;

  @Column(name = "vencedor", length = 255)
  private String vencedor;

  @Column(name = "registro_patrimonio", nullable = false)
  private Boolean registroPatrimonio = false;

  @Column(name = "registro_almoxarifado", nullable = false)
  private Boolean registroAlmoxarifado = false;

  @Column(name = "numero_reserva", length = 60)
  private String numeroReserva;

  @Column(name = "autorizacao_pagamento_numero", length = 100)
  private String autorizacaoPagamentoNumero;

  @Column(name = "autorizacao_pagamento_autor", length = 150)
  private String autorizacaoPagamentoAutor;

  @Column(name = "autorizacao_pagamento_data")
  private LocalDate autorizacaoPagamentoData;

  @Column(name = "autorizacao_pagamento_observacoes")
  private String autorizacaoPagamentoObservacoes;

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

  public String getPrioridade() {
    return prioridade;
  }

  public void setPrioridade(String prioridade) {
    this.prioridade = prioridade;
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
