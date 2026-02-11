package br.com.g3.rh.dto;

public class RhPontoDiaAtualizacaoRequest {
  private String ocorrencia;
  private String observacoes;

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
}
