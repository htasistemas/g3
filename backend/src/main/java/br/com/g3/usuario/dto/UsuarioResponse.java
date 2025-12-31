package br.com.g3.usuario.dto;

import java.time.LocalDateTime;
import java.util.List;

public class UsuarioResponse {
  private Long id;
  private String nomeUsuario;
  private String nome;
  private String email;
  private LocalDateTime criadoEm;
  private LocalDateTime atualizadoEm;
  private List<String> permissoes;

  public UsuarioResponse(
      Long id,
      String nomeUsuario,
      String nome,
      String email,
      LocalDateTime criadoEm,
      LocalDateTime atualizadoEm,
      List<String> permissoes) {
    this.id = id;
    this.nomeUsuario = nomeUsuario;
    this.nome = nome;
    this.email = email;
    this.criadoEm = criadoEm;
    this.atualizadoEm = atualizadoEm;
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

  public LocalDateTime getCriadoEm() {
    return criadoEm;
  }

  public LocalDateTime getAtualizadoEm() {
    return atualizadoEm;
  }

  public List<String> getPermissoes() {
    return permissoes;
  }
}
