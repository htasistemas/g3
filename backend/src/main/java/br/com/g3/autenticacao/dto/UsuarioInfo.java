package br.com.g3.autenticacao.dto;

import java.util.List;

public class UsuarioInfo {
  private Long id;
  private String nomeUsuario;
  private String nome;
  private String email;
  private List<String> permissoes;

  public UsuarioInfo(
      Long id, String nomeUsuario, String nome, String email, List<String> permissoes) {
    this.id = id;
    this.nomeUsuario = nomeUsuario;
    this.nome = nome;
    this.email = email;
    this.permissoes = permissoes;
  }

  public Long getId() {
    return id;
  }

  public String getNomeUsuario() {
    return nomeUsuario;
  }

  public String getNome() {
    return nome;
  }

  public String getEmail() {
    return email;
  }

  public List<String> getPermissoes() {
    return permissoes;
  }
}
