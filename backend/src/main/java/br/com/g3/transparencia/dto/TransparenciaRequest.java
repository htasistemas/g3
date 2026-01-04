package br.com.g3.transparencia.dto;

import java.math.BigDecimal;
import java.util.List;

public class TransparenciaRequest {
  private Long unidadeId;
  private BigDecimal totalRecebido;
  private String totalRecebidoHelper;
  private BigDecimal totalAplicado;
  private String totalAplicadoHelper;
  private BigDecimal saldoDisponivel;
  private String saldoDisponivelHelper;
  private BigDecimal prestadoMes;
  private String prestadoMesHelper;
  private List<TransparenciaRecebimentoRequest> recebimentos;
  private List<TransparenciaDestinacaoRequest> destinacoes;
  private List<TransparenciaComprovanteRequest> comprovantes;
  private List<TransparenciaTimelineRequest> timelines;
  private List<TransparenciaChecklistRequest> checklist;

  public Long getUnidadeId() {
    return unidadeId;
  }

  public void setUnidadeId(Long unidadeId) {
    this.unidadeId = unidadeId;
  }

  public BigDecimal getTotalRecebido() {
    return totalRecebido;
  }

  public void setTotalRecebido(BigDecimal totalRecebido) {
    this.totalRecebido = totalRecebido;
  }

  public String getTotalRecebidoHelper() {
    return totalRecebidoHelper;
  }

  public void setTotalRecebidoHelper(String totalRecebidoHelper) {
    this.totalRecebidoHelper = totalRecebidoHelper;
  }

  public BigDecimal getTotalAplicado() {
    return totalAplicado;
  }

  public void setTotalAplicado(BigDecimal totalAplicado) {
    this.totalAplicado = totalAplicado;
  }

  public String getTotalAplicadoHelper() {
    return totalAplicadoHelper;
  }

  public void setTotalAplicadoHelper(String totalAplicadoHelper) {
    this.totalAplicadoHelper = totalAplicadoHelper;
  }

  public BigDecimal getSaldoDisponivel() {
    return saldoDisponivel;
  }

  public void setSaldoDisponivel(BigDecimal saldoDisponivel) {
    this.saldoDisponivel = saldoDisponivel;
  }

  public String getSaldoDisponivelHelper() {
    return saldoDisponivelHelper;
  }

  public void setSaldoDisponivelHelper(String saldoDisponivelHelper) {
    this.saldoDisponivelHelper = saldoDisponivelHelper;
  }

  public BigDecimal getPrestadoMes() {
    return prestadoMes;
  }

  public void setPrestadoMes(BigDecimal prestadoMes) {
    this.prestadoMes = prestadoMes;
  }

  public String getPrestadoMesHelper() {
    return prestadoMesHelper;
  }

  public void setPrestadoMesHelper(String prestadoMesHelper) {
    this.prestadoMesHelper = prestadoMesHelper;
  }

  public List<TransparenciaRecebimentoRequest> getRecebimentos() {
    return recebimentos;
  }

  public void setRecebimentos(List<TransparenciaRecebimentoRequest> recebimentos) {
    this.recebimentos = recebimentos;
  }

  public List<TransparenciaDestinacaoRequest> getDestinacoes() {
    return destinacoes;
  }

  public void setDestinacoes(List<TransparenciaDestinacaoRequest> destinacoes) {
    this.destinacoes = destinacoes;
  }

  public List<TransparenciaComprovanteRequest> getComprovantes() {
    return comprovantes;
  }

  public void setComprovantes(List<TransparenciaComprovanteRequest> comprovantes) {
    this.comprovantes = comprovantes;
  }

  public List<TransparenciaTimelineRequest> getTimelines() {
    return timelines;
  }

  public void setTimelines(List<TransparenciaTimelineRequest> timelines) {
    this.timelines = timelines;
  }

  public List<TransparenciaChecklistRequest> getChecklist() {
    return checklist;
  }

  public void setChecklist(List<TransparenciaChecklistRequest> checklist) {
    this.checklist = checklist;
  }
}
