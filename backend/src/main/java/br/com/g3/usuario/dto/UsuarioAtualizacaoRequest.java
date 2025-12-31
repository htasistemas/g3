package br.com.g3.usuario.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

public class UsuarioAtualizacaoRequest {
  @NotBlank
  private String nome;

  @NotBlank
  @Email
  private String email;

  private String senha;
  @NotEmpty
  private List<String> permissoes = new ArrayList<>();

  public String getNomeUsuario() {
    return email;
  }

  public void setNomeUsuario(String nomeUsuario) {
    this.email = nomeUsuario;
  }

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

  public List<String> getPermissoes() {
    return permissoes;
  }

  public void setPermissoes(List<String> permissoes) {
    this.permissoes = permissoes;
  }
}
