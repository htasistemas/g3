package br.com.g3.usuario.dto;

import java.time.LocalDateTime;
import java.util.List;

public class UsuarioResponse {
  private Long id;
  private String nomeUsuario;
  private LocalDateTime criadoEm;
  private LocalDateTime atualizadoEm;
  private List<String> permissoes;

  public UsuarioResponse(
      Long id,
      String nomeUsuario,
      LocalDateTime criadoEm,
      LocalDateTime atualizadoEm,
      List<String> permissoes) {
    this.id = id;
    this.nomeUsuario = nomeUsuario;
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
