package br.com.g3.cursosatendimentos.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CursoAtendimentoStatusRequest {
  private String status;

  @JsonProperty("justification")
  private String justificativa;

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getJustificativa() {
    return justificativa;
  }

  public void setJustificativa(String justificativa) {
    this.justificativa = justificativa;
  }
}
