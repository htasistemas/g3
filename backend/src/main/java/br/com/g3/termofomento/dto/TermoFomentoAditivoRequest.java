package br.com.g3.termofomento.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

public class TermoFomentoAditivoRequest {
  @NotBlank
  private String tipoAditivo;

  @NotNull
  private LocalDate dataAditivo;

  private LocalDate novaDataFim;
  private BigDecimal novoValor;
  private String observacoes;
  private TermoDocumentoRequest anexo;

  public String getTipoAditivo() {
    return tipoAditivo;
  }

  public void setTipoAditivo(String tipoAditivo) {
    this.tipoAditivo = tipoAditivo;
  }

  public LocalDate getDataAditivo() {
    return dataAditivo;
  }

  public void setDataAditivo(LocalDate dataAditivo) {
    this.dataAditivo = dataAditivo;
  }

  public LocalDate getNovaDataFim() {
    return novaDataFim;
  }

  public void setNovaDataFim(LocalDate novaDataFim) {
    this.novaDataFim = novaDataFim;
  }

  public BigDecimal getNovoValor() {
    return novoValor;
  }

  public void setNovoValor(BigDecimal novoValor) {
    this.novoValor = novoValor;
  }

  public String getObservacoes() {
    return observacoes;
  }

  public void setObservacoes(String observacoes) {
    this.observacoes = observacoes;
  }

  public TermoDocumentoRequest getAnexo() {
    return anexo;
  }

  public void setAnexo(TermoDocumentoRequest anexo) {
    this.anexo = anexo;
  }
}
