package br.com.g3.planotrabalho.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "plano_trabalho_cronograma")
public class PlanoTrabalhoCronograma {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "plano_trabalho_id", nullable = false)
  private Long planoTrabalhoId;

  @Column(name = "referencia_tipo", length = 40)
  private String referenciaTipo;

  @Column(name = "referencia_id", length = 40)
  private String referenciaId;

  @Column(name = "referencia_descricao", columnDefinition = "TEXT")
  private String referenciaDescricao;

  @Column(name = "competencia", length = 20, nullable = false)
  private String competencia;

  @Column(name = "descricao_resumida", columnDefinition = "TEXT")
  private String descricaoResumida;

  @Column(name = "valor_previsto", precision = 14, scale = 2)
  private BigDecimal valorPrevisto;

  @Column(name = "fonte_recurso", length = 60)
  private String fonteRecurso;

  @Column(name = "natureza_despesa", length = 120)
  private String naturezaDespesa;

  @Column(name = "observacoes", columnDefinition = "TEXT")
  private String observacoes;

  @Column(name = "criado_em", nullable = false)
  private LocalDateTime criadoEm;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getPlanoTrabalhoId() {
    return planoTrabalhoId;
  }

  public void setPlanoTrabalhoId(Long planoTrabalhoId) {
    this.planoTrabalhoId = planoTrabalhoId;
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

  public LocalDateTime getCriadoEm() {
    return criadoEm;
  }

  public void setCriadoEm(LocalDateTime criadoEm) {
    this.criadoEm = criadoEm;
  }
}
