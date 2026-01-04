package br.com.g3.termofomento.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class TermoFomentoRequest {
  @NotBlank
  private String numeroTermo;

  @NotBlank
  private String tipoTermo;

  private String orgaoConcedente;
  private LocalDate dataAssinatura;
  private LocalDate dataInicioVigencia;
  private LocalDate dataFimVigencia;

  @NotBlank
  private String situacao;

  private String descricaoObjeto;
  private BigDecimal valorGlobal;
  private String responsavelInterno;

  @Valid
  private TermoDocumentoRequest termoDocumento;

  @Valid
  private List<TermoDocumentoRequest> documentosRelacionados;

  @Valid
  private List<TermoFomentoAditivoRequest> aditivos;

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

  public TermoDocumentoRequest getTermoDocumento() {
    return termoDocumento;
  }

  public void setTermoDocumento(TermoDocumentoRequest termoDocumento) {
    this.termoDocumento = termoDocumento;
  }

  public List<TermoDocumentoRequest> getDocumentosRelacionados() {
    return documentosRelacionados;
  }

  public void setDocumentosRelacionados(List<TermoDocumentoRequest> documentosRelacionados) {
    this.documentosRelacionados = documentosRelacionados;
  }

  public List<TermoFomentoAditivoRequest> getAditivos() {
    return aditivos;
  }

  public void setAditivos(List<TermoFomentoAditivoRequest> aditivos) {
    this.aditivos = aditivos;
  }
}
