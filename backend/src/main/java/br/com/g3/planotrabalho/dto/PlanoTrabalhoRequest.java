package br.com.g3.planotrabalho.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

public class PlanoTrabalhoRequest {
  private Long id;

  @NotBlank
  private String codigoInterno;

  @NotBlank
  private String titulo;

  @NotBlank
  private String descricaoGeral;

  @NotBlank
  private String status;

  private String orgaoConcedente;
  private String orgaoOutroDescricao;
  private String areaPrograma;
  private LocalDate dataElaboracao;
  private LocalDate dataAprovacao;
  private LocalDate vigenciaInicio;
  private LocalDate vigenciaFim;

  @NotNull
  private Long termoFomentoId;

  private String numeroProcesso;
  private String modalidade;
  private String observacoesVinculacao;

  @Valid
  private List<PlanoTrabalhoMetaRequest> metas;

  @Valid
  private List<PlanoTrabalhoCronogramaRequest> cronograma;

  @Valid
  private List<PlanoTrabalhoEquipeRequest> equipe;

  private String arquivoFormato;

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

  public List<PlanoTrabalhoMetaRequest> getMetas() {
    return metas;
  }

  public void setMetas(List<PlanoTrabalhoMetaRequest> metas) {
    this.metas = metas;
  }

  public List<PlanoTrabalhoCronogramaRequest> getCronograma() {
    return cronograma;
  }

  public void setCronograma(List<PlanoTrabalhoCronogramaRequest> cronograma) {
    this.cronograma = cronograma;
  }

  public List<PlanoTrabalhoEquipeRequest> getEquipe() {
    return equipe;
  }

  public void setEquipe(List<PlanoTrabalhoEquipeRequest> equipe) {
    this.equipe = equipe;
  }

  public String getArquivoFormato() {
    return arquivoFormato;
  }

  public void setArquivoFormato(String arquivoFormato) {
    this.arquivoFormato = arquivoFormato;
  }
}
