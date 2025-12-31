package br.com.g3.autenticacao.service;

import br.com.g3.autenticacao.dto.LoginRequest;
import br.com.g3.autenticacao.dto.LoginResponse;
import br.com.g3.autenticacao.dto.CadastroContaRequest;
import br.com.g3.autenticacao.dto.RecuperarSenhaRequest;
import br.com.g3.autenticacao.dto.RedefinirSenhaRequest;

public interface AutenticacaoService {
  LoginResponse autenticar(LoginRequest request);

  void cadastrarConta(CadastroContaRequest request);

  void solicitarRecuperacaoSenha(RecuperarSenhaRequest request);

  void redefinirSenha(RedefinirSenhaRequest request);
}
