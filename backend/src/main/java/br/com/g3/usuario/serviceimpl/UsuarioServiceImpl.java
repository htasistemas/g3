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
    usuario.setHorarioEntrada1(request.getHorarioEntrada1());
    usuario.setHorarioSaida1(request.getHorarioSaida1());
    usuario.setHorarioEntrada2(request.getHorarioEntrada2());
    usuario.setHorarioSaida2(request.getHorarioSaida2());
    usuario.setHorarioSegundaEntrada1(request.getHorarioSegundaEntrada1());
    usuario.setHorarioSegundaSaida1(request.getHorarioSegundaSaida1());
    usuario.setHorarioSegundaEntrada2(request.getHorarioSegundaEntrada2());
    usuario.setHorarioSegundaSaida2(request.getHorarioSegundaSaida2());
    usuario.setHorarioTercaEntrada1(request.getHorarioTercaEntrada1());
    usuario.setHorarioTercaSaida1(request.getHorarioTercaSaida1());
    usuario.setHorarioTercaEntrada2(request.getHorarioTercaEntrada2());
    usuario.setHorarioTercaSaida2(request.getHorarioTercaSaida2());
    usuario.setHorarioQuartaEntrada1(request.getHorarioQuartaEntrada1());
    usuario.setHorarioQuartaSaida1(request.getHorarioQuartaSaida1());
    usuario.setHorarioQuartaEntrada2(request.getHorarioQuartaEntrada2());
    usuario.setHorarioQuartaSaida2(request.getHorarioQuartaSaida2());
    usuario.setHorarioQuintaEntrada1(request.getHorarioQuintaEntrada1());
    usuario.setHorarioQuintaSaida1(request.getHorarioQuintaSaida1());
    usuario.setHorarioQuintaEntrada2(request.getHorarioQuintaEntrada2());
    usuario.setHorarioQuintaSaida2(request.getHorarioQuintaSaida2());
    usuario.setHorarioSextaEntrada1(request.getHorarioSextaEntrada1());
    usuario.setHorarioSextaSaida1(request.getHorarioSextaSaida1());
    usuario.setHorarioSextaEntrada2(request.getHorarioSextaEntrada2());
    usuario.setHorarioSextaSaida2(request.getHorarioSextaSaida2());
    usuario.setHorarioSabadoEntrada1(request.getHorarioSabadoEntrada1());
    usuario.setHorarioSabadoSaida1(request.getHorarioSabadoSaida1());
    usuario.setHorarioSabadoEntrada2(request.getHorarioSabadoEntrada2());
    usuario.setHorarioSabadoSaida2(request.getHorarioSabadoSaida2());
    usuario.setHorarioDomingoEntrada1(request.getHorarioDomingoEntrada1());
    usuario.setHorarioDomingoSaida1(request.getHorarioDomingoSaida1());
    usuario.setHorarioDomingoEntrada2(request.getHorarioDomingoEntrada2());
    usuario.setHorarioDomingoSaida2(request.getHorarioDomingoSaida2());
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
    usuario.setHorarioEntrada1(request.getHorarioEntrada1());
    usuario.setHorarioSaida1(request.getHorarioSaida1());
    usuario.setHorarioEntrada2(request.getHorarioEntrada2());
    usuario.setHorarioSaida2(request.getHorarioSaida2());
    usuario.setHorarioSegundaEntrada1(request.getHorarioSegundaEntrada1());
    usuario.setHorarioSegundaSaida1(request.getHorarioSegundaSaida1());
    usuario.setHorarioSegundaEntrada2(request.getHorarioSegundaEntrada2());
    usuario.setHorarioSegundaSaida2(request.getHorarioSegundaSaida2());
    usuario.setHorarioTercaEntrada1(request.getHorarioTercaEntrada1());
    usuario.setHorarioTercaSaida1(request.getHorarioTercaSaida1());
    usuario.setHorarioTercaEntrada2(request.getHorarioTercaEntrada2());
    usuario.setHorarioTercaSaida2(request.getHorarioTercaSaida2());
    usuario.setHorarioQuartaEntrada1(request.getHorarioQuartaEntrada1());
    usuario.setHorarioQuartaSaida1(request.getHorarioQuartaSaida1());
    usuario.setHorarioQuartaEntrada2(request.getHorarioQuartaEntrada2());
    usuario.setHorarioQuartaSaida2(request.getHorarioQuartaSaida2());
    usuario.setHorarioQuintaEntrada1(request.getHorarioQuintaEntrada1());
    usuario.setHorarioQuintaSaida1(request.getHorarioQuintaSaida1());
    usuario.setHorarioQuintaEntrada2(request.getHorarioQuintaEntrada2());
    usuario.setHorarioQuintaSaida2(request.getHorarioQuintaSaida2());
    usuario.setHorarioSextaEntrada1(request.getHorarioSextaEntrada1());
    usuario.setHorarioSextaSaida1(request.getHorarioSextaSaida1());
    usuario.setHorarioSextaEntrada2(request.getHorarioSextaEntrada2());
    usuario.setHorarioSextaSaida2(request.getHorarioSextaSaida2());
    usuario.setHorarioSabadoEntrada1(request.getHorarioSabadoEntrada1());
    usuario.setHorarioSabadoSaida1(request.getHorarioSabadoSaida1());
    usuario.setHorarioSabadoEntrada2(request.getHorarioSabadoEntrada2());
    usuario.setHorarioSabadoSaida2(request.getHorarioSabadoSaida2());
    usuario.setHorarioDomingoEntrada1(request.getHorarioDomingoEntrada1());
    usuario.setHorarioDomingoSaida1(request.getHorarioDomingoSaida1());
    usuario.setHorarioDomingoEntrada2(request.getHorarioDomingoEntrada2());
    usuario.setHorarioDomingoSaida2(request.getHorarioDomingoSaida2());

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
