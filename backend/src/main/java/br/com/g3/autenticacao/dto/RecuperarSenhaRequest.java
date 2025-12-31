package br.com.g3.autenticacao.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class RecuperarSenhaRequest {
  @NotBlank(message = "Email e obrigatorio")
  @Email(message = "Email invalido")
  private String email;

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }
}
