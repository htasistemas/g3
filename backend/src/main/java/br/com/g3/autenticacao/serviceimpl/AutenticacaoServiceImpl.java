package br.com.g3.autenticacao.serviceimpl;

import br.com.g3.autenticacao.dto.LoginRequest;
import br.com.g3.autenticacao.dto.LoginResponse;
import br.com.g3.autenticacao.dto.UsuarioInfo;
import br.com.g3.autenticacao.service.AutenticacaoService;
import br.com.g3.usuario.domain.Usuario;
import br.com.g3.usuario.repository.UsuarioRepository;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AutenticacaoServiceImpl implements AutenticacaoService {
  private final UsuarioRepository repository;
  private final PasswordEncoder passwordEncoder;

  public AutenticacaoServiceImpl(UsuarioRepository repository, PasswordEncoder passwordEncoder) {
    this.repository = repository;
    this.passwordEncoder = passwordEncoder;
  }

  @Override
  public LoginResponse autenticar(LoginRequest request) {
    Usuario usuario =
        repository
            .buscarPorNomeUsuarioIgnoreCase(request.getNomeUsuario())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credenciais inválidas"));

    if (!passwordEncoder.matches(request.getSenha(), usuario.getSenhaHash())) {
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credenciais inválidas");
    }

    String token = UUID.randomUUID().toString();
    List<String> permissoes =
        usuario.getPermissoes().stream().map(p -> p.getNome()).collect(Collectors.toList());
    UsuarioInfo usuarioInfo = new UsuarioInfo(usuario.getId(), usuario.getNomeUsuario(), permissoes);
    return new LoginResponse(token, usuarioInfo);
  }
}
