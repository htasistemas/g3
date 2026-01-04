package br.com.g3.planotrabalho.dto;

import java.math.BigDecimal;

public class PlanoTrabalhoCronogramaResponse {
  private final Long id;
  private final String referenciaTipo;
  private final String referenciaId;
  private final String referenciaDescricao;
  private final String competencia;
  private final String descricaoResumida;
  private final BigDecimal valorPrevisto;
  private final String fonteRecurso;
  private final String naturezaDespesa;
  private final String observacoes;

  public PlanoTrabalhoCronogramaResponse(
      Long id,
      String referenciaTipo,
      String referenciaId,
      String referenciaDescricao,
      String competencia,
      String descricaoResumida,
      BigDecimal valorPrevisto,
      String fonteRecurso,
      String naturezaDespesa,
      String observacoes) {
    this.id = id;
    this.referenciaTipo = referenciaTipo;
    this.referenciaId = referenciaId;
    this.referenciaDescricao = referenciaDescricao;
    this.competencia = competencia;
    this.descricaoResumida = descricaoResumida;
    this.valorPrevisto = valorPrevisto;
    this.fonteRecurso = fonteRecurso;
    this.naturezaDespesa = naturezaDespesa;
    this.observacoes = observacoes;
  }

  public Long getId() {
    return id;
  }

  public String getReferenciaTipo() {
    return referenciaTipo;
  }

  public String getReferenciaId() {
    return referenciaId;
  }

  public String getReferenciaDescricao() {
    return referenciaDescricao;
  }

  public String getCompetencia() {
    return competencia;
  }

  public String getDescricaoResumida() {
    return descricaoResumida;
  }

  public BigDecimal getValorPrevisto() {
    return valorPrevisto;
  }

  public String getFonteRecurso() {
    return fonteRecurso;
  }

  public String getNaturezaDespesa() {
    return naturezaDespesa;
  }

  public String getObservacoes() {
    return observacoes;
  }
}
