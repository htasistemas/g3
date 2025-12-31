package br.com.g3.usuario.serviceimpl;

import br.com.g3.usuario.domain.Permissao;
import br.com.g3.usuario.domain.Usuario;
import br.com.g3.usuario.dto.UsuarioAtualizacaoRequest;
import br.com.g3.usuario.dto.UsuarioCriacaoRequest;
import br.com.g3.usuario.dto.UsuarioResponse;
import br.com.g3.usuario.mapper.UsuarioMapper;
import br.com.g3.usuario.repository.UsuarioRepository;
import br.com.g3.usuario.service.PermissaoService;
import br.com.g3.usuario.service.UsuarioService;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class UsuarioServiceImpl implements UsuarioService {
  private final UsuarioRepository repository;
  private final PasswordEncoder passwordEncoder;
  private final PermissaoService permissaoService;

  public UsuarioServiceImpl(
      UsuarioRepository repository,
      PasswordEncoder passwordEncoder,
      PermissaoService permissaoService) {
    this.repository = repository;
    this.passwordEncoder = passwordEncoder;
    this.permissaoService = permissaoService;
  }

  @Override
  public List<UsuarioResponse> listar() {
    return repository.listar().stream().map(UsuarioMapper::toResponse).collect(Collectors.toList());
  }

  @Override
  @Transactional
  public UsuarioResponse criar(UsuarioCriacaoRequest request) {
    String email = request.getEmail().trim().toLowerCase();
    repository
        .buscarPorEmailIgnoreCase(email)
        .ifPresent(
            usuario -> {
              throw new ResponseStatusException(
                  HttpStatus.CONFLICT, "Ja existe um usuario com este email");
            });

    Usuario usuario = new Usuario();
    usuario.setNomeUsuario(email);
    usuario.setNome(request.getNome());
    usuario.setEmail(email);
    usuario.setSenhaHash(passwordEncoder.encode(request.getSenha()));
    usuario.setPermissoes(montarPermissoes(request.getPermissoes()));
    LocalDateTime agora = LocalDateTime.now();
    usuario.setCriadoEm(agora);
    usuario.setAtualizadoEm(agora);

    Usuario salvo = repository.salvar(usuario);
    return UsuarioMapper.toResponse(salvo);
  }

  @Override
  @Transactional
  public UsuarioResponse atualizar(Long id, UsuarioAtualizacaoRequest request) {
    Usuario usuario =
        repository
            .buscarPorId(id)
            .orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario nao encontrado"));

    String email = request.getEmail().trim().toLowerCase();
    if (usuario.getEmail() == null || !usuario.getEmail().equalsIgnoreCase(email)) {
      repository
          .buscarPorEmailIgnoreCase(email)
          .ifPresent(
              existente -> {
                if (!existente.getId().equals(usuario.getId())) {
                  throw new ResponseStatusException(
                      HttpStatus.CONFLICT, "Ja existe um usuario com este email");
                }
              });
      usuario.setEmail(email);
      usuario.setNomeUsuario(email);
    }

    usuario.setNome(request.getNome());

    String senha = request.getSenha();
    if (senha != null && !senha.trim().isEmpty()) {
      usuario.setSenhaHash(passwordEncoder.encode(senha));
    }

    usuario.setPermissoes(montarPermissoes(request.getPermissoes()));

    usuario.setAtualizadoEm(LocalDateTime.now());
    Usuario salvo = repository.salvar(usuario);
    return UsuarioMapper.toResponse(salvo);
  }

  @Override
  @Transactional
  public void remover(Long id) {
    Usuario usuario =
        repository
            .buscarPorId(id)
            .orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario nao encontrado"));
    repository.remover(usuario);
  }

  private Set<Permissao> montarPermissoes(List<String> nomes) {
    List<Permissao> permissoes = permissaoService.buscarPorNomes(nomes);
    return new HashSet<>(permissoes);
  }
}
