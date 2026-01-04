package br.com.g3.planotrabalho.dto;

import java.math.BigDecimal;

public class PlanoTrabalhoCronogramaRequest {
  private Long id;
  private String referenciaTipo;
  private String referenciaId;
  private String referenciaDescricao;
  private String competencia;
  private String descricaoResumida;
  private BigDecimal valorPrevisto;
  private String fonteRecurso;
  private String naturezaDespesa;
  private String observacoes;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getReferenciaTipo() {
    return referenciaTipo;
  }

  public void setReferenciaTipo(String referenciaTipo) {
    this.referenciaTipo = referenciaTipo;
  }

  public String getReferenciaId() {
    return referenciaId;
  }

  public void setReferenciaId(String referenciaId) {
    this.referenciaId = referenciaId;
  }

  public String getReferenciaDescricao() {
    return referenciaDescricao;
  }

  public void setReferenciaDescricao(String referenciaDescricao) {
    this.referenciaDescricao = referenciaDescricao;
  }

  public String getCompetencia() {
    return competencia;
  }

  public void setCompetencia(String competencia) {
    this.competencia = competencia;
  }

  public String getDescricaoResumida() {
    return descricaoResumida;
  }

  public void setDescricaoResumida(String descricaoResumida) {
    this.descricaoResumida = descricaoResumida;
  }

  public BigDecimal getValorPrevisto() {
    return valorPrevisto;
  }

  public void setValorPrevisto(BigDecimal valorPrevisto) {
    this.valorPrevisto = valorPrevisto;
  }

  public String getFonteRecurso() {
    return fonteRecurso;
  }

  public void setFonteRecurso(String fonteRecurso) {
    this.fonteRecurso = fonteRecurso;
  }

  public String getNaturezaDespesa() {
    return naturezaDespesa;
  }

  public void setNaturezaDespesa(String naturezaDespesa) {
    this.naturezaDespesa = naturezaDespesa;
  }

  public String getObservacoes() {
    return observacoes;
  }

  public void setObservacoes(String observacoes) {
    this.observacoes = observacoes;
  }
}
