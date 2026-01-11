package br.com.g3.emprestimoseventos.dto;

import java.time.LocalDateTime;
import java.util.List;

public class EmprestimoEventoResponse {
  private Long id;
  private EventoEmprestimoResponse evento;
  private Long unidadeId;
  private ResponsavelResumoResponse responsavel;
  private LocalDateTime dataRetiradaPrevista;
  private LocalDateTime dataDevolucaoPrevista;
  private LocalDateTime dataRetiradaReal;
  private LocalDateTime dataDevolucaoReal;
  private String status;
  private String observacoes;
  private List<EmprestimoEventoItemResponse> itens;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public EventoEmprestimoResponse getEvento() {
    return evento;
  }

  public void setEvento(EventoEmprestimoResponse evento) {
    this.evento = evento;
  }

  public Long getUnidadeId() {
    return unidadeId;
  }

  public void setUnidadeId(Long unidadeId) {
    this.unidadeId = unidadeId;
  }

  public ResponsavelResumoResponse getResponsavel() {
    return responsavel;
  }

  public void setResponsavel(ResponsavelResumoResponse responsavel) {
    this.responsavel = responsavel;
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

  public List<EmprestimoEventoItemResponse> getItens() {
    return itens;
  }

  public void setItens(List<EmprestimoEventoItemResponse> itens) {
    this.itens = itens;
  }
}
