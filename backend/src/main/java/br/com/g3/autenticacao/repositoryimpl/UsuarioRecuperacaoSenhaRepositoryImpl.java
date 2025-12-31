package br.com.g3.autenticacao.repositoryimpl;

import br.com.g3.autenticacao.domain.UsuarioRecuperacaoSenha;
import br.com.g3.autenticacao.repository.UsuarioRecuperacaoSenhaRepository;
import br.com.g3.usuario.domain.Usuario;
import java.time.LocalDateTime;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class UsuarioRecuperacaoSenhaRepositoryImpl implements UsuarioRecuperacaoSenhaRepository {
  private final UsuarioRecuperacaoSenhaJpaRepository jpaRepository;

  public UsuarioRecuperacaoSenhaRepositoryImpl(
      UsuarioRecuperacaoSenhaJpaRepository jpaRepository) {
    this.jpaRepository = jpaRepository;
  }

  @Override
  public UsuarioRecuperacaoSenha salvar(UsuarioRecuperacaoSenha token) {
    return jpaRepository.save(token);
  }

  @Override
  public Optional<UsuarioRecuperacaoSenha> buscarPorToken(String token) {
    return jpaRepository.findTopByToken(token);
  }

  @Override
  public Optional<UsuarioRecuperacaoSenha> buscarUltimoPorUsuario(Usuario usuario) {
    return jpaRepository.findTopByUsuarioOrderByCriadoEmDesc(usuario);
  }

  @Override
  public void marcarTokensComoUsados(Usuario usuario, LocalDateTime usadoEm) {
    jpaRepository.marcarTokensComoUsados(usuario, usadoEm);
  }
}
