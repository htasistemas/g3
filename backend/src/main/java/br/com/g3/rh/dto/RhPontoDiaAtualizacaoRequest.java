package br.com.g3.rh.dto;

public class RhPontoDiaAtualizacaoRequest {
  private String ocorrencia;
  private String observacoes;
  private String senhaAdmin;
  private String justificativa;
  private String entrada1;
  private String saida1;
  private String entrada2;
  private String saida2;

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

  public String getSenhaAdmin() {
    return senhaAdmin;
  }

  public void setSenhaAdmin(String senhaAdmin) {
    this.senhaAdmin = senhaAdmin;
  }

  public String getJustificativa() {
    return justificativa;
  }

  public void setJustificativa(String justificativa) {
    this.justificativa = justificativa;
  }

  public String getEntrada1() {
    return entrada1;
  }

  public void setEntrada1(String entrada1) {
    this.entrada1 = entrada1;
  }

  public String getSaida1() {
    return saida1;
  }

  public void setSaida1(String saida1) {
    this.saida1 = saida1;
  }

  public String getEntrada2() {
    return entrada2;
  }

  public void setEntrada2(String entrada2) {
    this.entrada2 = entrada2;
  }

  public String getSaida2() {
    return saida2;
  }

  public void setSaida2(String saida2) {
    this.saida2 = saida2;
  }
}
