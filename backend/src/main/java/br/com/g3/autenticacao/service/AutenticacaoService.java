package br.com.g3.autenticacao.service;

import br.com.g3.autenticacao.dto.LoginRequest;
import br.com.g3.autenticacao.dto.LoginResponse;

public interface AutenticacaoService {
  LoginResponse autenticar(LoginRequest request);
}
