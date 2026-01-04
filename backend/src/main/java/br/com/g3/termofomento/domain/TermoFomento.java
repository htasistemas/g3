package br.com.g3.termofomento.domain;

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
@Table(name = "termo_fomento")
public class TermoFomento {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "numero_termo", nullable = false, length = 120)
  private String numeroTermo;

  @Column(name = "tipo_termo", nullable = false, length = 20)
  private String tipoTermo;

  @Column(name = "orgao_concedente", length = 200)
  private String orgaoConcedente;

  @Column(name = "data_assinatura")
  private LocalDate dataAssinatura;

  @Column(name = "data_inicio_vigencia")
  private LocalDate dataInicioVigencia;

  @Column(name = "data_fim_vigencia")
  private LocalDate dataFimVigencia;

  @Column(name = "situacao", nullable = false, length = 20)
  private String situacao;

  @Column(name = "descricao_objeto", columnDefinition = "TEXT")
  private String descricaoObjeto;

  @Column(name = "valor_global", precision = 14, scale = 2)
  private BigDecimal valorGlobal;

  @Column(name = "responsavel_interno", length = 200)
  private String responsavelInterno;

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

  public String getNumeroTermo() {
    return numeroTermo;
  }

  public void setNumeroTermo(String numeroTermo) {
    this.numeroTermo = numeroTermo;
  }

  public String getTipoTermo() {
    return tipoTermo;
  }

  public void setTipoTermo(String tipoTermo) {
    this.tipoTermo = tipoTermo;
  }

  public String getOrgaoConcedente() {
    return orgaoConcedente;
  }

  public void setOrgaoConcedente(String orgaoConcedente) {
    this.orgaoConcedente = orgaoConcedente;
  }

  public LocalDate getDataAssinatura() {
    return dataAssinatura;
  }

  public void setDataAssinatura(LocalDate dataAssinatura) {
    this.dataAssinatura = dataAssinatura;
  }

  public LocalDate getDataInicioVigencia() {
    return dataInicioVigencia;
  }

  public void setDataInicioVigencia(LocalDate dataInicioVigencia) {
    this.dataInicioVigencia = dataInicioVigencia;
  }

  public LocalDate getDataFimVigencia() {
    return dataFimVigencia;
  }

  public void setDataFimVigencia(LocalDate dataFimVigencia) {
    this.dataFimVigencia = dataFimVigencia;
  }

  public String getSituacao() {
    return situacao;
  }

  public void setSituacao(String situacao) {
    this.situacao = situacao;
  }

  public String getDescricaoObjeto() {
    return descricaoObjeto;
  }

  public void setDescricaoObjeto(String descricaoObjeto) {
    this.descricaoObjeto = descricaoObjeto;
  }

  public BigDecimal getValorGlobal() {
    return valorGlobal;
  }

  public void setValorGlobal(BigDecimal valorGlobal) {
    this.valorGlobal = valorGlobal;
  }

  public String getResponsavelInterno() {
    return responsavelInterno;
  }

  public void setResponsavelInterno(String responsavelInterno) {
    this.responsavelInterno = responsavelInterno;
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
