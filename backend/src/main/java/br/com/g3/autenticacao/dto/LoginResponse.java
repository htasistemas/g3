package br.com.g3.autenticacao.dto;

public class LoginResponse {
  private String token;
  private UsuarioInfo usuario;

  public LoginResponse(String token, UsuarioInfo usuario) {
    this.token = token;
    this.usuario = usuario;
  }

  public String getToken() {
    return token;
  }

  public UsuarioInfo getUsuario() {
    return usuario;
  }
}
