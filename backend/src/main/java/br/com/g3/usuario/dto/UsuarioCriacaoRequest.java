package br.com.g3.usuario.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

public class UsuarioCriacaoRequest {
  @NotBlank
  private String nomeUsuario;

  @NotBlank
  private String senha;
  @NotEmpty
  private List<String> permissoes = new ArrayList<>();

  public String getNomeUsuario() {
    return nomeUsuario;
  }

  public void setNomeUsuario(String nomeUsuario) {
    this.nomeUsuario = nomeUsuario;
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
