package br.com.g3.cadastrovoluntario.dto;

public class CadastroVoluntarioConsultaResponse {
  private final CadastroVoluntarioResponse voluntario;

  public CadastroVoluntarioConsultaResponse(CadastroVoluntarioResponse voluntario) {
    this.voluntario = voluntario;
  }

  public CadastroVoluntarioResponse getVoluntario() {
    return voluntario;
  }
}
