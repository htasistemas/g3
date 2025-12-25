package br.com.g3.usuario.dto;

public class PermissaoResponse {
  private Long id;
  private String nome;

  public PermissaoResponse(Long id, String nome) {
    this.id = id;
    this.nome = nome;
  }

  public Long getId() {
    return id;
  }

  public String getNome() {
    return nome;
  }
}
