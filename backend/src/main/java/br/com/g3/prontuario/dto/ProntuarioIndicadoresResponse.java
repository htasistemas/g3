package br.com.g3.prontuario.dto;

import java.time.LocalDateTime;

public class ProntuarioIndicadoresResponse {
  private long totalAtendimentos;
  private long totalDoacoes;
  private long totalCestas;
  private long totalCursos;
  private long totalEncaminhamentos;
  private Double taxaEncaminhamentosConcluidos;
  private Double tempoMedioRetornoDias;
  private LocalDateTime ultimoContato;
  private long pendenciasAbertas;
  private String classificacaoRiscoAtual;
  private String statusAcompanhamento;

  public long getTotalAtendimentos() {
    return totalAtendimentos;
  }

  public void setTotalAtendimentos(long totalAtendimentos) {
    this.totalAtendimentos = totalAtendimentos;
  }

  public long getTotalDoacoes() {
    return totalDoacoes;
  }

  public void setTotalDoacoes(long totalDoacoes) {
    this.totalDoacoes = totalDoacoes;
  }

  public long getTotalCestas() {
    return totalCestas;
  }

  public void setTotalCestas(long totalCestas) {
    this.totalCestas = totalCestas;
  }

  public long getTotalCursos() {
    return totalCursos;
  }

  public void setTotalCursos(long totalCursos) {
    this.totalCursos = totalCursos;
  }

  public long getTotalEncaminhamentos() {
    return totalEncaminhamentos;
  }

  public void setTotalEncaminhamentos(long totalEncaminhamentos) {
    this.totalEncaminhamentos = totalEncaminhamentos;
  }

  public Double getTaxaEncaminhamentosConcluidos() {
    return taxaEncaminhamentosConcluidos;
  }

  public void setTaxaEncaminhamentosConcluidos(Double taxaEncaminhamentosConcluidos) {
    this.taxaEncaminhamentosConcluidos = taxaEncaminhamentosConcluidos;
  }

  public Double getTempoMedioRetornoDias() {
    return tempoMedioRetornoDias;
  }

  public void setTempoMedioRetornoDias(Double tempoMedioRetornoDias) {
    this.tempoMedioRetornoDias = tempoMedioRetornoDias;
  }

  public LocalDateTime getUltimoContato() {
    return ultimoContato;
  }

  public void setUltimoContato(LocalDateTime ultimoContato) {
    this.ultimoContato = ultimoContato;
  }

  public long getPendenciasAbertas() {
    return pendenciasAbertas;
  }

  public void setPendenciasAbertas(long pendenciasAbertas) {
    this.pendenciasAbertas = pendenciasAbertas;
  }

  public String getClassificacaoRiscoAtual() {
    return classificacaoRiscoAtual;
  }

  public void setClassificacaoRiscoAtual(String classificacaoRiscoAtual) {
    this.classificacaoRiscoAtual = classificacaoRiscoAtual;
  }

  public String getStatusAcompanhamento() {
    return statusAcompanhamento;
  }

  public void setStatusAcompanhamento(String statusAcompanhamento) {
    this.statusAcompanhamento = statusAcompanhamento;
  }
}
