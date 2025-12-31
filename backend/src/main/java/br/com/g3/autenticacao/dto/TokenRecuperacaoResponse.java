package br.com.g3.autenticacao.dto;

import java.time.LocalDateTime;

public class TokenRecuperacaoResponse {
  private String email;
  private String token;
  private LocalDateTime expiraEm;
  private LocalDateTime usadoEm;
  private LocalDateTime criadoEm;

  public TokenRecuperacaoResponse(
      String email,
      String token,
      LocalDateTime expiraEm,
      LocalDateTime usadoEm,
      LocalDateTime criadoEm) {
    this.email = email;
    this.token = token;
    this.expiraEm = expiraEm;
    this.usadoEm = usadoEm;
    this.criadoEm = criadoEm;
  }

  public String getEmail() {
    return email;
  }

  public String getToken() {
    return token;
  }

  public LocalDateTime getExpiraEm() {
    return expiraEm;
  }

  public LocalDateTime getUsadoEm() {
    return usadoEm;
  }

  public LocalDateTime getCriadoEm() {
    return criadoEm;
  }
}
