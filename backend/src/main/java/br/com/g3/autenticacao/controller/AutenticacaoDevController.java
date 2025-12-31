package br.com.g3.autenticacao.controller;

import br.com.g3.autenticacao.domain.UsuarioRecuperacaoSenha;
import br.com.g3.autenticacao.dto.TokenRecuperacaoResponse;
import br.com.g3.autenticacao.repository.UsuarioRecuperacaoSenhaRepository;
import br.com.g3.usuario.domain.Usuario;
import br.com.g3.usuario.repository.UsuarioRepository;
import java.util.Optional;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@Profile("dev")
@RequestMapping("/api/auth/dev")
public class AutenticacaoDevController {
  private final UsuarioRepository usuarioRepository;
  private final UsuarioRecuperacaoSenhaRepository recuperacaoRepository;

  public AutenticacaoDevController(
      UsuarioRepository usuarioRepository,
      UsuarioRecuperacaoSenhaRepository recuperacaoRepository) {
    this.usuarioRepository = usuarioRepository;
    this.recuperacaoRepository = recuperacaoRepository;
  }

  @GetMapping("/token-recuperacao")
  public TokenRecuperacaoResponse buscarToken(@RequestParam("email") String email) {
    Usuario usuario =
        usuarioRepository
            .buscarPorEmailIgnoreCase(email.trim())
            .orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Email nao encontrado"));

    Optional<UsuarioRecuperacaoSenha> tokenOpt =
        recuperacaoRepository.buscarUltimoPorUsuario(usuario);
    UsuarioRecuperacaoSenha token =
        tokenOpt.orElseThrow(
            () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Token nao encontrado"));

    return new TokenRecuperacaoResponse(
        usuario.getEmail(),
        token.getToken(),
        token.getExpiraEm(),
        token.getUsadoEm(),
        token.getCriadoEm());
  }
}
