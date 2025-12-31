package br.com.g3.usuario.repositoryimpl;

import br.com.g3.usuario.domain.Usuario;
import br.com.g3.usuario.repository.UsuarioRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class UsuarioRepositoryImpl implements UsuarioRepository {
  private final UsuarioJpaRepository jpaRepository;

  public UsuarioRepositoryImpl(UsuarioJpaRepository jpaRepository) {
    this.jpaRepository = jpaRepository;
  }

  @Override
  public Usuario salvar(Usuario usuario) {
    return jpaRepository.save(usuario);
  }

  @Override
  public List<Usuario> listar() {
    return jpaRepository.findAll();
  }

  @Override
  public Optional<Usuario> buscarPorId(Long id) {
    return jpaRepository.findById(id);
  }

  @Override
  public Optional<Usuario> buscarPorNomeUsuarioIgnoreCase(String nomeUsuario) {
    return jpaRepository.findTopByNomeUsuarioIgnoreCase(nomeUsuario);
  }

  @Override
  public Optional<Usuario> buscarPorEmailIgnoreCase(String email) {
    return jpaRepository.findTopByEmailIgnoreCase(email);
  }

  @Override
  public void remover(Usuario usuario) {
    jpaRepository.delete(usuario);
  }
}
