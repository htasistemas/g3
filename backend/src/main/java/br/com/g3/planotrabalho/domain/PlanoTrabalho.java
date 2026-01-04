package br.com.g3.planotrabalho.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "plano_trabalho")
public class PlanoTrabalho {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "codigo_interno", nullable = false, length = 40)
  private String codigoInterno;

  @Column(name = "titulo", nullable = false, length = 200)
  private String titulo;

  @Column(name = "descricao_geral", columnDefinition = "TEXT", nullable = false)
  private String descricaoGeral;

  @Column(name = "status", nullable = false, length = 20)
  private String status;

  @Column(name = "orgao_concedente", length = 40)
  private String orgaoConcedente;

  @Column(name = "orgao_outro_descricao", length = 200)
  private String orgaoOutroDescricao;

  @Column(name = "area_programa", length = 120)
  private String areaPrograma;

  @Column(name = "data_elaboracao")
  private LocalDate dataElaboracao;

  @Column(name = "data_aprovacao")
  private LocalDate dataAprovacao;

  @Column(name = "vigencia_inicio")
  private LocalDate vigenciaInicio;

  @Column(name = "vigencia_fim")
  private LocalDate vigenciaFim;

  @Column(name = "termo_fomento_id", nullable = false)
  private Long termoFomentoId;

  @Column(name = "numero_processo", length = 120)
  private String numeroProcesso;

  @Column(name = "modalidade", length = 120)
  private String modalidade;

  @Column(name = "observacoes_vinculacao", columnDefinition = "TEXT")
  private String observacoesVinculacao;

  @Column(name = "arquivo_formato", length = 20)
  private String arquivoFormato;

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

  public String getCodigoInterno() {
    return codigoInterno;
  }

  public void setCodigoInterno(String codigoInterno) {
    this.codigoInterno = codigoInterno;
  }

  public String getTitulo() {
    return titulo;
  }

  public void setTitulo(String titulo) {
    this.titulo = titulo;
  }

  public String getDescricaoGeral() {
    return descricaoGeral;
  }

  public void setDescricaoGeral(String descricaoGeral) {
    this.descricaoGeral = descricaoGeral;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getOrgaoConcedente() {
    return orgaoConcedente;
  }

  public void setOrgaoConcedente(String orgaoConcedente) {
    this.orgaoConcedente = orgaoConcedente;
  }

  public String getOrgaoOutroDescricao() {
    return orgaoOutroDescricao;
  }

  public void setOrgaoOutroDescricao(String orgaoOutroDescricao) {
    this.orgaoOutroDescricao = orgaoOutroDescricao;
  }

  public String getAreaPrograma() {
    return areaPrograma;
  }

  public void setAreaPrograma(String areaPrograma) {
    this.areaPrograma = areaPrograma;
  }

  public LocalDate getDataElaboracao() {
    return dataElaboracao;
  }

  public void setDataElaboracao(LocalDate dataElaboracao) {
    this.dataElaboracao = dataElaboracao;
  }

  public LocalDate getDataAprovacao() {
    return dataAprovacao;
  }

  public void setDataAprovacao(LocalDate dataAprovacao) {
    this.dataAprovacao = dataAprovacao;
  }

  public LocalDate getVigenciaInicio() {
    return vigenciaInicio;
  }

  public void setVigenciaInicio(LocalDate vigenciaInicio) {
    this.vigenciaInicio = vigenciaInicio;
  }

  public LocalDate getVigenciaFim() {
    return vigenciaFim;
  }

  public void setVigenciaFim(LocalDate vigenciaFim) {
    this.vigenciaFim = vigenciaFim;
  }

  public Long getTermoFomentoId() {
    return termoFomentoId;
  }

  public void setTermoFomentoId(Long termoFomentoId) {
    this.termoFomentoId = termoFomentoId;
  }

  public String getNumeroProcesso() {
    return numeroProcesso;
  }

  public void setNumeroProcesso(String numeroProcesso) {
    this.numeroProcesso = numeroProcesso;
  }

  public String getModalidade() {
    return modalidade;
  }

  public void setModalidade(String modalidade) {
    this.modalidade = modalidade;
  }

  public String getObservacoesVinculacao() {
    return observacoesVinculacao;
  }

  public void setObservacoesVinculacao(String observacoesVinculacao) {
    this.observacoesVinculacao = observacoesVinculacao;
  }

  public String getArquivoFormato() {
    return arquivoFormato;
  }

  public void setArquivoFormato(String arquivoFormato) {
    this.arquivoFormato = arquivoFormato;
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
