package br.com.g3.termofomento.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class TermoFomentoResponse {
  private final Long id;
  private final String numeroTermo;
  private final String tipoTermo;
  private final String orgaoConcedente;
  private final LocalDate dataAssinatura;
  private final LocalDate dataInicioVigencia;
  private final LocalDate dataFimVigencia;
  private final String situacao;
  private final String descricaoObjeto;
  private final BigDecimal valorGlobal;
  private final String responsavelInterno;
  private final TermoDocumentoResponse termoDocumento;
  private final List<TermoDocumentoResponse> documentosRelacionados;
  private final List<TermoFomentoAditivoResponse> aditivos;
  private final LocalDateTime criadoEm;
  private final LocalDateTime atualizadoEm;

  public TermoFomentoResponse(
      Long id,
      String numeroTermo,
      String tipoTermo,
      String orgaoConcedente,
      LocalDate dataAssinatura,
      LocalDate dataInicioVigencia,
      LocalDate dataFimVigencia,
      String situacao,
      String descricaoObjeto,
      BigDecimal valorGlobal,
      String responsavelInterno,
      TermoDocumentoResponse termoDocumento,
      List<TermoDocumentoResponse> documentosRelacionados,
      List<TermoFomentoAditivoResponse> aditivos,
      LocalDateTime criadoEm,
      LocalDateTime atualizadoEm) {
    this.id = id;
    this.numeroTermo = numeroTermo;
    this.tipoTermo = tipoTermo;
    this.orgaoConcedente = orgaoConcedente;
    this.dataAssinatura = dataAssinatura;
    this.dataInicioVigencia = dataInicioVigencia;
    this.dataFimVigencia = dataFimVigencia;
    this.situacao = situacao;
    this.descricaoObjeto = descricaoObjeto;
    this.valorGlobal = valorGlobal;
    this.responsavelInterno = responsavelInterno;
    this.termoDocumento = termoDocumento;
    this.documentosRelacionados = documentosRelacionados;
    this.aditivos = aditivos;
    this.criadoEm = criadoEm;
    this.atualizadoEm = atualizadoEm;
  }

  public Long getId() {
    return id;
  }

  public String getNumeroTermo() {
    return numeroTermo;
  }

  public String getTipoTermo() {
    return tipoTermo;
  }

  public String getOrgaoConcedente() {
    return orgaoConcedente;
  }

  public LocalDate getDataAssinatura() {
    return dataAssinatura;
  }

  public LocalDate getDataInicioVigencia() {
    return dataInicioVigencia;
  }

  public LocalDate getDataFimVigencia() {
    return dataFimVigencia;
  }

  public String getSituacao() {
    return situacao;
  }

  public String getDescricaoObjeto() {
    return descricaoObjeto;
  }

  public BigDecimal getValorGlobal() {
    return valorGlobal;
  }

  public String getResponsavelInterno() {
    return responsavelInterno;
  }

  public TermoDocumentoResponse getTermoDocumento() {
    return termoDocumento;
  }

  public List<TermoDocumentoResponse> getDocumentosRelacionados() {
    return documentosRelacionados;
  }

  public List<TermoFomentoAditivoResponse> getAditivos() {
    return aditivos;
  }

  public LocalDateTime getCriadoEm() {
    return criadoEm;
  }

  public LocalDateTime getAtualizadoEm() {
    return atualizadoEm;
  }
}
