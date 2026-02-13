package br.com.g3.rhcontratacao.dto;

import java.time.LocalDateTime;

public class RhFichaAdmissaoResponse {
  private Long id;
  private Long processoId;
  private String dadosPessoaisJson;
  private String dependentesJson;
  private String dadosInternosJson;
  private LocalDateTime criadoEm;
  private LocalDateTime atualizadoEm;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getProcessoId() {
    return processoId;
  }

  public void setProcessoId(Long processoId) {
    this.processoId = processoId;
  }

  public String getDadosPessoaisJson() {
    return dadosPessoaisJson;
  }

  public void setDadosPessoaisJson(String dadosPessoaisJson) {
    this.dadosPessoaisJson = dadosPessoaisJson;
  }

  public String getDependentesJson() {
    return dependentesJson;
  }

  public void setDependentesJson(String dependentesJson) {
    this.dependentesJson = dependentesJson;
  }

  public String getDadosInternosJson() {
    return dadosInternosJson;
  }

  public void setDadosInternosJson(String dadosInternosJson) {
    this.dadosInternosJson = dadosInternosJson;
  }

  public LocalDateTime getCriadoEm() {
    return criadoEm;
  }

  public void setCriadoEm(LocalDateTime criadoEm) {
    this.criadoEm = criadoEm;
  }

  public LocalDateTime getAtualizadoEm() {
    return atualizadoEm;
  }

  public void setAtualizadoEm(LocalDateTime atualizadoEm) {
    this.atualizadoEm = atualizadoEm;
  }
}
