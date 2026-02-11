package br.com.g3.rh.dto;

import java.util.ArrayList;
import java.util.List;

public class RhPontoEspelhoResponse {
  private Long funcionarioId;
  private int mes;
  private int ano;
  private Integer totalTrabalhadoMinutos;
  private Integer totalDevidoMinutos;
  private Integer totalExtrasMinutos;
  private Integer totalFaltasAtrasosMinutos;
  private Integer diasTrabalhados;
  private List<RhPontoDiaResumoResponse> dias = new ArrayList<>();

  public Long getFuncionarioId() {
    return funcionarioId;
  }

  public void setFuncionarioId(Long funcionarioId) {
    this.funcionarioId = funcionarioId;
  }

  public int getMes() {
    return mes;
  }

  public void setMes(int mes) {
    this.mes = mes;
  }

  public int getAno() {
    return ano;
  }

  public void setAno(int ano) {
    this.ano = ano;
  }

  public Integer getTotalTrabalhadoMinutos() {
    return totalTrabalhadoMinutos;
  }

  public void setTotalTrabalhadoMinutos(Integer totalTrabalhadoMinutos) {
    this.totalTrabalhadoMinutos = totalTrabalhadoMinutos;
  }

  public Integer getTotalDevidoMinutos() {
    return totalDevidoMinutos;
  }

  public void setTotalDevidoMinutos(Integer totalDevidoMinutos) {
    this.totalDevidoMinutos = totalDevidoMinutos;
  }

  public Integer getTotalExtrasMinutos() {
    return totalExtrasMinutos;
  }

  public void setTotalExtrasMinutos(Integer totalExtrasMinutos) {
    this.totalExtrasMinutos = totalExtrasMinutos;
  }

  public Integer getTotalFaltasAtrasosMinutos() {
    return totalFaltasAtrasosMinutos;
  }

  public void setTotalFaltasAtrasosMinutos(Integer totalFaltasAtrasosMinutos) {
    this.totalFaltasAtrasosMinutos = totalFaltasAtrasosMinutos;
  }

  public Integer getDiasTrabalhados() {
    return diasTrabalhados;
  }

  public void setDiasTrabalhados(Integer diasTrabalhados) {
    this.diasTrabalhados = diasTrabalhados;
  }

  public List<RhPontoDiaResumoResponse> getDias() {
    return dias;
  }

  public void setDias(List<RhPontoDiaResumoResponse> dias) {
    this.dias = dias;
  }
}
