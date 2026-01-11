package br.com.g3.emprestimoseventos.dto;

import java.time.LocalDateTime;
import java.util.List;

public class EmprestimoEventoRequest {
  private Long eventoId;
  private Long unidadeId;
  private Long responsavelId;
  private LocalDateTime dataRetiradaPrevista;
  private LocalDateTime dataDevolucaoPrevista;
  private LocalDateTime dataRetiradaReal;
  private LocalDateTime dataDevolucaoReal;
  private String status;
  private String observacoes;
  private List<EmprestimoEventoItemRequest> itens;

  public Long getEventoId() {
    return eventoId;
  }

  public void setEventoId(Long eventoId) {
    this.eventoId = eventoId;
  }

  public Long getUnidadeId() {
    return unidadeId;
  }

  public void setUnidadeId(Long unidadeId) {
    this.unidadeId = unidadeId;
  }

  public Long getResponsavelId() {
    return responsavelId;
  }

  public void setResponsavelId(Long responsavelId) {
    this.responsavelId = responsavelId;
  }

  public LocalDateTime getDataRetiradaPrevista() {
    return dataRetiradaPrevista;
  }

  public void setDataRetiradaPrevista(LocalDateTime dataRetiradaPrevista) {
    this.dataRetiradaPrevista = dataRetiradaPrevista;
  }

  public LocalDateTime getDataDevolucaoPrevista() {
    return dataDevolucaoPrevista;
  }

  public void setDataDevolucaoPrevista(LocalDateTime dataDevolucaoPrevista) {
    this.dataDevolucaoPrevista = dataDevolucaoPrevista;
  }

  public LocalDateTime getDataRetiradaReal() {
    return dataRetiradaReal;
  }

  public void setDataRetiradaReal(LocalDateTime dataRetiradaReal) {
    this.dataRetiradaReal = dataRetiradaReal;
  }

  public LocalDateTime getDataDevolucaoReal() {
    return dataDevolucaoReal;
  }

  public void setDataDevolucaoReal(LocalDateTime dataDevolucaoReal) {
    this.dataDevolucaoReal = dataDevolucaoReal;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getObservacoes() {
    return observacoes;
  }

  public void setObservacoes(String observacoes) {
    this.observacoes = observacoes;
  }

  public List<EmprestimoEventoItemRequest> getItens() {
    return itens;
  }

  public void setItens(List<EmprestimoEventoItemRequest> itens) {
    this.itens = itens;
  }
}
