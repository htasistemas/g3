package br.com.g3.termofomento.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class TermoFomentoAditivoResponse {
  private final Long id;
  private final String tipoAditivo;
  private final LocalDate dataAditivo;
  private final LocalDate novaDataFim;
  private final BigDecimal novoValor;
  private final String observacoes;
  private final TermoDocumentoResponse anexo;

  public TermoFomentoAditivoResponse(
      Long id,
      String tipoAditivo,
      LocalDate dataAditivo,
      LocalDate novaDataFim,
      BigDecimal novoValor,
      String observacoes,
      TermoDocumentoResponse anexo) {
    this.id = id;
    this.tipoAditivo = tipoAditivo;
    this.dataAditivo = dataAditivo;
    this.novaDataFim = novaDataFim;
    this.novoValor = novoValor;
    this.observacoes = observacoes;
    this.anexo = anexo;
  }

  public Long getId() {
    return id;
  }

  public String getTipoAditivo() {
    return tipoAditivo;
  }

  public LocalDate getDataAditivo() {
    return dataAditivo;
  }

  public LocalDate getNovaDataFim() {
    return novaDataFim;
  }

  public BigDecimal getNovoValor() {
    return novoValor;
  }

  public String getObservacoes() {
    return observacoes;
  }

  public TermoDocumentoResponse getAnexo() {
    return anexo;
  }
}
