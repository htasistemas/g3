package br.com.g3.autenticacao.service;

public interface GoogleTokenService {
  GoogleUsuarioInfo validarIdToken(String idToken);

  record GoogleUsuarioInfo(String googleId, String email, String nome, String fotoUrl) {}
}
