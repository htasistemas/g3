package br.com.g3.autenticacao.serviceimpl;

import br.com.g3.autenticacao.domain.UsuarioRecuperacaoSenha;
import br.com.g3.autenticacao.dto.CadastroContaRequest;
import br.com.g3.autenticacao.dto.LoginRequest;
import br.com.g3.autenticacao.dto.LoginResponse;
import br.com.g3.autenticacao.dto.RecuperarSenhaRequest;
import br.com.g3.autenticacao.dto.RedefinirSenhaRequest;
import br.com.g3.autenticacao.dto.UsuarioInfo;
import br.com.g3.autenticacao.repository.UsuarioRecuperacaoSenhaRepository;
import br.com.g3.autenticacao.service.AutenticacaoService;
import br.com.g3.usuario.domain.Usuario;
import br.com.g3.usuario.repository.UsuarioRepository;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AutenticacaoServiceImpl implements AutenticacaoService {
  private final UsuarioRepository repository;
  private final UsuarioRecuperacaoSenhaRepository recuperacaoRepository;
  private final PasswordEncoder passwordEncoder;
  private final SecureRandom secureRandom = new SecureRandom();

  public AutenticacaoServiceImpl(
      UsuarioRepository repository,
      UsuarioRecuperacaoSenhaRepository recuperacaoRepository,
      PasswordEncoder passwordEncoder) {
    this.repository = repository;
    this.recuperacaoRepository = recuperacaoRepository;
    this.passwordEncoder = passwordEncoder;
  }

  @Override
  public LoginResponse autenticar(LoginRequest request) {
    Usuario usuario = buscarUsuarioPorLogin(request.getNomeUsuario());

    if (!passwordEncoder.matches(request.getSenha(), usuario.getSenhaHash())) {
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credenciais invalidas");
    }

    String token = UUID.randomUUID().toString();
    List<String> permissoes =
        usuario.getPermissoes().stream().map(p -> p.getNome()).collect(Collectors.toList());
    UsuarioInfo usuarioInfo =
        new UsuarioInfo(
            usuario.getId(), usuario.getNomeUsuario(), usuario.getNome(), usuario.getEmail(), permissoes);
    return new LoginResponse(token, usuarioInfo);
  }

  @Override
  @Transactional
  public void cadastrarConta(CadastroContaRequest request) {
    String email = request.getEmail().trim().toLowerCase();

    repository
        .buscarPorEmailIgnoreCase(email)
        .ifPresent(
            usuario -> {
              throw new ResponseStatusException(HttpStatus.CONFLICT, "Email ja cadastrado");
            });

    repository
        .buscarPorNomeUsuarioIgnoreCase(email)
        .ifPresent(
            usuario -> {
              throw new ResponseStatusException(HttpStatus.CONFLICT, "Email ja cadastrado");
            });

    Usuario usuario = new Usuario();
    usuario.setNomeUsuario(email);
    usuario.setNome(request.getNome().trim());
    usuario.setEmail(email);
    usuario.setSenhaHash(passwordEncoder.encode(request.getSenha()));
    LocalDateTime agora = LocalDateTime.now();
    usuario.setCriadoEm(agora);
    usuario.setAtualizadoEm(agora);

    repository.salvar(usuario);
  }

  @Override
  @Transactional
  public void solicitarRecuperacaoSenha(RecuperarSenhaRequest request) {
    String email = request.getEmail().trim().toLowerCase();
    Usuario usuario = repository.buscarPorEmailIgnoreCase(email).orElse(null);
    if (usuario == null) {
      return;
    }

    LocalDateTime agora = LocalDateTime.now();
    recuperacaoRepository.marcarTokensComoUsados(usuario, agora);

    UsuarioRecuperacaoSenha token = new UsuarioRecuperacaoSenha();
    token.setUsuario(usuario);
    token.setToken(gerarTokenSeguro());
    token.setCriadoEm(agora);
    token.setExpiraEm(agora.plusHours(2));
    recuperacaoRepository.salvar(token);
  }

  @Override
  @Transactional
  public void redefinirSenha(RedefinirSenhaRequest request) {
    if (!request.getSenha().equals(request.getConfirmarSenha())) {
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST, "Senha e confirmacao nao conferem");
    }

    UsuarioRecuperacaoSenha token =
        recuperacaoRepository
            .buscarPorToken(request.getToken())
            .orElseThrow(
                () ->
                    new ResponseStatusException(
                        HttpStatus.UNPROCESSABLE_ENTITY, "Token invalido ou expirado"));

    if (token.getUsadoEm() != null || token.getExpiraEm().isBefore(LocalDateTime.now())) {
      throw new ResponseStatusException(
          HttpStatus.UNPROCESSABLE_ENTITY, "Token invalido ou expirado");
    }

    Usuario usuario = token.getUsuario();
    usuario.setSenhaHash(passwordEncoder.encode(request.getSenha()));
    usuario.setAtualizadoEm(LocalDateTime.now());
    repository.salvar(usuario);

    token.setUsadoEm(LocalDateTime.now());
    recuperacaoRepository.salvar(token);
  }

  private Usuario buscarUsuarioPorLogin(String login) {
    String valor = login == null ? "" : login.trim();
    return repository
        .buscarPorNomeUsuarioIgnoreCase(valor)
        .orElseGet(
            () ->
                repository
                    .buscarPorEmailIgnoreCase(valor)
                    .orElseThrow(
                        () ->
                            new ResponseStatusException(
                                HttpStatus.UNAUTHORIZED, "Credenciais invalidas")));
  }

  private String gerarTokenSeguro() {
    byte[] bytes = new byte[32];
    secureRandom.nextBytes(bytes);
    return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
  }
}
