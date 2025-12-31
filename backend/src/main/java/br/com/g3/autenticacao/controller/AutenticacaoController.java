package br.com.g3.autenticacao.controller;

import br.com.g3.autenticacao.dto.CadastroContaRequest;
import br.com.g3.autenticacao.dto.LoginRequest;
import br.com.g3.autenticacao.dto.LoginResponse;
import br.com.g3.autenticacao.dto.RecuperarSenhaRequest;
import br.com.g3.autenticacao.dto.RedefinirSenhaRequest;
import br.com.g3.autenticacao.service.AutenticacaoService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AutenticacaoController {
  private final AutenticacaoService autenticacaoService;

  public AutenticacaoController(AutenticacaoService autenticacaoService) {
    this.autenticacaoService = autenticacaoService;
  }

  @PostMapping("/login")
  public LoginResponse login(@Valid @RequestBody LoginRequest request) {
    return autenticacaoService.autenticar(request);
  }

  @PostMapping("/registrar")
  public void registrar(@Valid @RequestBody CadastroContaRequest request) {
    autenticacaoService.cadastrarConta(request);
  }

  @PostMapping("/recuperar-senha")
  public void recuperarSenha(@Valid @RequestBody RecuperarSenhaRequest request) {
    autenticacaoService.solicitarRecuperacaoSenha(request);
  }

  @PostMapping("/redefinir-senha")
  public void redefinirSenha(@Valid @RequestBody RedefinirSenhaRequest request) {
    autenticacaoService.redefinirSenha(request);
  }
}
