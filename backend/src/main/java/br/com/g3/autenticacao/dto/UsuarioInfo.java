package br.com.g3.autenticacao.dto;

import java.util.List;

public class UsuarioInfo {
  private Long id;
  private String nomeUsuario;
  private List<String> permissoes;

  public UsuarioInfo(Long id, String nomeUsuario, List<String> permissoes) {
    this.id = id;
    this.nomeUsuario = nomeUsuario;
    this.permissoes = permissoes;
  }

  public Long getId() {
    return id;
  }

  public String getNomeUsuario() {
    return nomeUsuario;
  }

  public List<String> getPermissoes() {
    return permissoes;
  }
}
