package br.com.g3.autenticacao.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class RedefinirSenhaRequest {
  @NotBlank(message = "Token e obrigatorio")
  private String token;

  @NotBlank(message = "Senha e obrigatoria")
  @Size(min = 6, message = "Senha deve ter no minimo 6 caracteres")
  private String senha;

  @NotBlank(message = "Confirmacao de senha e obrigatoria")
  private String confirmarSenha;

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }

  public String getSenha() {
    return senha;
  }

  public void setSenha(String senha) {
    this.senha = senha;
  }

  public String getConfirmarSenha() {
    return confirmarSenha;
  }

  public void setConfirmarSenha(String confirmarSenha) {
    this.confirmarSenha = confirmarSenha;
  }
}
