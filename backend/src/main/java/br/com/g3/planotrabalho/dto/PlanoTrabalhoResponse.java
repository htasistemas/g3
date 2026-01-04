package br.com.g3.planotrabalho.dto;

import java.time.LocalDate;
import java.util.List;

public class PlanoTrabalhoResponse {
  private final Long id;
  private final String codigoInterno;
  private final String titulo;
  private final String descricaoGeral;
  private final String status;
  private final String orgaoConcedente;
  private final String orgaoOutroDescricao;
  private final String areaPrograma;
  private final LocalDate dataElaboracao;
  private final LocalDate dataAprovacao;
  private final LocalDate vigenciaInicio;
  private final LocalDate vigenciaFim;
  private final Long termoFomentoId;
  private final String numeroProcesso;
  private final String modalidade;
  private final String observacoesVinculacao;
  private final List<PlanoTrabalhoMetaResponse> metas;
  private final List<PlanoTrabalhoCronogramaResponse> cronograma;
  private final List<PlanoTrabalhoEquipeResponse> equipe;
  private final String arquivoFormato;
  private final PlanoTrabalhoTermoResumoResponse termoFomento;

  public PlanoTrabalhoResponse(
      Long id,
      String codigoInterno,
      String titulo,
      String descricaoGeral,
      String status,
      String orgaoConcedente,
      String orgaoOutroDescricao,
      String areaPrograma,
      LocalDate dataElaboracao,
      LocalDate dataAprovacao,
      LocalDate vigenciaInicio,
      LocalDate vigenciaFim,
      Long termoFomentoId,
      String numeroProcesso,
      String modalidade,
      String observacoesVinculacao,
      List<PlanoTrabalhoMetaResponse> metas,
      List<PlanoTrabalhoCronogramaResponse> cronograma,
      List<PlanoTrabalhoEquipeResponse> equipe,
      String arquivoFormato,
      PlanoTrabalhoTermoResumoResponse termoFomento) {
    this.id = id;
    this.codigoInterno = codigoInterno;
    this.titulo = titulo;
    this.descricaoGeral = descricaoGeral;
    this.status = status;
    this.orgaoConcedente = orgaoConcedente;
    this.orgaoOutroDescricao = orgaoOutroDescricao;
    this.areaPrograma = areaPrograma;
    this.dataElaboracao = dataElaboracao;
    this.dataAprovacao = dataAprovacao;
    this.vigenciaInicio = vigenciaInicio;
    this.vigenciaFim = vigenciaFim;
    this.termoFomentoId = termoFomentoId;
    this.numeroProcesso = numeroProcesso;
    this.modalidade = modalidade;
    this.observacoesVinculacao = observacoesVinculacao;
    this.metas = metas;
    this.cronograma = cronograma;
    this.equipe = equipe;
    this.arquivoFormato = arquivoFormato;
    this.termoFomento = termoFomento;
  }

  public Long getId() {
    return id;
  }

  public String getCodigoInterno() {
    return codigoInterno;
  }

  public String getTitulo() {
    return titulo;
  }

  public String getDescricaoGeral() {
    return descricaoGeral;
  }

  public String getStatus() {
    return status;
  }

  public String getOrgaoConcedente() {
    return orgaoConcedente;
  }

  public String getOrgaoOutroDescricao() {
    return orgaoOutroDescricao;
  }

  public String getAreaPrograma() {
    return areaPrograma;
  }

  public LocalDate getDataElaboracao() {
    return dataElaboracao;
  }

  public LocalDate getDataAprovacao() {
    return dataAprovacao;
  }

  public LocalDate getVigenciaInicio() {
    return vigenciaInicio;
  }

  public LocalDate getVigenciaFim() {
    return vigenciaFim;
  }

  public Long getTermoFomentoId() {
    return termoFomentoId;
  }

  public String getNumeroProcesso() {
    return numeroProcesso;
  }

  public String getModalidade() {
    return modalidade;
  }

  public String getObservacoesVinculacao() {
    return observacoesVinculacao;
  }

  public List<PlanoTrabalhoMetaResponse> getMetas() {
    return metas;
  }

  public List<PlanoTrabalhoCronogramaResponse> getCronograma() {
    return cronograma;
  }

  public List<PlanoTrabalhoEquipeResponse> getEquipe() {
    return equipe;
  }

  public String getArquivoFormato() {
    return arquivoFormato;
  }

  public PlanoTrabalhoTermoResumoResponse getTermoFomento() {
    return termoFomento;
  }
}
