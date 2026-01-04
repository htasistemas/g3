package br.com.g3.transparencia.dto;

import java.math.BigDecimal;
import java.util.List;

public class TransparenciaResponse {
  private final Long id;
  private final Long unidadeId;
  private final BigDecimal totalRecebido;
  private final String totalRecebidoHelper;
  private final BigDecimal totalAplicado;
  private final String totalAplicadoHelper;
  private final BigDecimal saldoDisponivel;
  private final String saldoDisponivelHelper;
  private final BigDecimal prestadoMes;
  private final String prestadoMesHelper;
  private final List<TransparenciaRecebimentoResponse> recebimentos;
  private final List<TransparenciaDestinacaoResponse> destinacoes;
  private final List<TransparenciaComprovanteResponse> comprovantes;
  private final List<TransparenciaTimelineResponse> timelines;
  private final List<TransparenciaChecklistResponse> checklist;

  public TransparenciaResponse(
      Long id,
      Long unidadeId,
      BigDecimal totalRecebido,
      String totalRecebidoHelper,
      BigDecimal totalAplicado,
      String totalAplicadoHelper,
      BigDecimal saldoDisponivel,
      String saldoDisponivelHelper,
      BigDecimal prestadoMes,
      String prestadoMesHelper,
      List<TransparenciaRecebimentoResponse> recebimentos,
      List<TransparenciaDestinacaoResponse> destinacoes,
      List<TransparenciaComprovanteResponse> comprovantes,
      List<TransparenciaTimelineResponse> timelines,
      List<TransparenciaChecklistResponse> checklist) {
    this.id = id;
    this.unidadeId = unidadeId;
    this.totalRecebido = totalRecebido;
    this.totalRecebidoHelper = totalRecebidoHelper;
    this.totalAplicado = totalAplicado;
    this.totalAplicadoHelper = totalAplicadoHelper;
    this.saldoDisponivel = saldoDisponivel;
    this.saldoDisponivelHelper = saldoDisponivelHelper;
    this.prestadoMes = prestadoMes;
    this.prestadoMesHelper = prestadoMesHelper;
    this.recebimentos = recebimentos;
    this.destinacoes = destinacoes;
    this.comprovantes = comprovantes;
    this.timelines = timelines;
    this.checklist = checklist;
  }

  public Long getId() {
    return id;
  }

  public Long getUnidadeId() {
    return unidadeId;
  }

  public BigDecimal getTotalRecebido() {
    return totalRecebido;
  }

  public String getTotalRecebidoHelper() {
    return totalRecebidoHelper;
  }

  public BigDecimal getTotalAplicado() {
    return totalAplicado;
  }

  public String getTotalAplicadoHelper() {
    return totalAplicadoHelper;
  }

  public BigDecimal getSaldoDisponivel() {
    return saldoDisponivel;
  }

  public String getSaldoDisponivelHelper() {
    return saldoDisponivelHelper;
  }

  public BigDecimal getPrestadoMes() {
    return prestadoMes;
  }

  public String getPrestadoMesHelper() {
    return prestadoMesHelper;
  }

  public List<TransparenciaRecebimentoResponse> getRecebimentos() {
    return recebimentos;
  }

  public List<TransparenciaDestinacaoResponse> getDestinacoes() {
    return destinacoes;
  }

  public List<TransparenciaComprovanteResponse> getComprovantes() {
    return comprovantes;
  }

  public List<TransparenciaTimelineResponse> getTimelines() {
    return timelines;
  }

  public List<TransparenciaChecklistResponse> getChecklist() {
    return checklist;
  }
}
