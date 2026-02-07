package br.com.g3.autenticacao.dto;

import jakarta.validation.constraints.NotBlank;

public class GoogleLoginRequest {
  @NotBlank(message = "Token do Google e obrigatorio")
  private String idToken;

  public String getIdToken() {
    return idToken;
  }

  public void setIdToken(String idToken) {
    this.idToken = idToken;
  }
}
