package br.com.g3.planotrabalho.dto;

import java.util.List;

public class PlanoTrabalhoListaResponse {
  private final List<PlanoTrabalhoResponse> planos;

  public PlanoTrabalhoListaResponse(List<PlanoTrabalhoResponse> planos) {
    this.planos = planos;
  }

  public List<PlanoTrabalhoResponse> getPlanos() {
    return planos;
  }
}
