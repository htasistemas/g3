package br.com.g3.autenticacao.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CadastroContaRequest {
  @NotBlank(message = "Nome e obrigatorio")
  private String nome;

  @NotBlank(message = "Email e obrigatorio")
  @Email(message = "Email invalido")
  private String email;

  @NotBlank(message = "Senha e obrigatoria")
  @Size(min = 6, message = "Senha deve ter no minimo 6 caracteres")
  private String senha;

  public String getNome() {
    return nome;
  }

  public void setNome(String nome) {
    this.nome = nome;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getSenha() {
    return senha;
  }

  public void setSenha(String senha) {
    this.senha = senha;
  }
}
