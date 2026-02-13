package br.com.g3.rhcontratacao.dto;

public class RhFichaAdmissaoRequest {
  private String dadosPessoaisJson;
  private String dependentesJson;
  private String dadosInternosJson;

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
}
