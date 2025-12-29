package br.com.g3.cadastrovoluntario.dto;

import java.util.List;

public class CadastroVoluntarioListaResponse {
  private final List<CadastroVoluntarioResponse> voluntarios;

  public CadastroVoluntarioListaResponse(List<CadastroVoluntarioResponse> voluntarios) {
    this.voluntarios = voluntarios;
  }

  public List<CadastroVoluntarioResponse> getVoluntarios() {
    return voluntarios;
  }
}
