package br.com.g3.rh.dto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class RhPontoDiaResponse {
  private Long id;
  private Long funcionarioId;
  private LocalDate data;
  private String ocorrencia;
  private String observacoes;
  private Integer cargaPrevistaMinutos;
  private Integer toleranciaMinutos;
  private Integer totalTrabalhadoMinutos;
  private Integer extrasMinutos;
  private Integer faltasAtrasosMinutos;
  private List<RhPontoMarcacaoResponse> marcacoes = new ArrayList<>();

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getFuncionarioId() {
    return funcionarioId;
  }

  public void setFuncionarioId(Long funcionarioId) {
    this.funcionarioId = funcionarioId;
  }

  public LocalDate getData() {
    return data;
  }

  public void setData(LocalDate data) {
    this.data = data;
  }

  public String getOcorrencia() {
    return ocorrencia;
  }

  public void setOcorrencia(String ocorrencia) {
    this.ocorrencia = ocorrencia;
  }

  public String getObservacoes() {
    return observacoes;
  }

  public void setObservacoes(String observacoes) {
    this.observacoes = observacoes;
  }

  public Integer getCargaPrevistaMinutos() {
    return cargaPrevistaMinutos;
  }

  public void setCargaPrevistaMinutos(Integer cargaPrevistaMinutos) {
    this.cargaPrevistaMinutos = cargaPrevistaMinutos;
  }

  public Integer getToleranciaMinutos() {
    return toleranciaMinutos;
  }

  public void setToleranciaMinutos(Integer toleranciaMinutos) {
    this.toleranciaMinutos = toleranciaMinutos;
  }

  public Integer getTotalTrabalhadoMinutos() {
    return totalTrabalhadoMinutos;
  }

  public void setTotalTrabalhadoMinutos(Integer totalTrabalhadoMinutos) {
    this.totalTrabalhadoMinutos = totalTrabalhadoMinutos;
  }

  public Integer getExtrasMinutos() {
    return extrasMinutos;
  }

  public void setExtrasMinutos(Integer extrasMinutos) {
    this.extrasMinutos = extrasMinutos;
  }

  public Integer getFaltasAtrasosMinutos() {
    return faltasAtrasosMinutos;
  }

  public void setFaltasAtrasosMinutos(Integer faltasAtrasosMinutos) {
    this.faltasAtrasosMinutos = faltasAtrasosMinutos;
  }

  public List<RhPontoMarcacaoResponse> getMarcacoes() {
    return marcacoes;
  }

  public void setMarcacoes(List<RhPontoMarcacaoResponse> marcacoes) {
    this.marcacoes = marcacoes;
  }
}
