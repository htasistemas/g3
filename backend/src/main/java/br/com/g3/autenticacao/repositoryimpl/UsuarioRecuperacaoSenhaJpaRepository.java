package br.com.g3.autenticacao.repositoryimpl;

import br.com.g3.autenticacao.domain.UsuarioRecuperacaoSenha;
import br.com.g3.usuario.domain.Usuario;
import java.time.LocalDateTime;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRecuperacaoSenhaJpaRepository
    extends JpaRepository<UsuarioRecuperacaoSenha, Long> {
  Optional<UsuarioRecuperacaoSenha> findTopByToken(String token);

  Optional<UsuarioRecuperacaoSenha> findTopByUsuarioOrderByCriadoEmDesc(Usuario usuario);

  @Modifying
  @Query(
      "update UsuarioRecuperacaoSenha t set t.usadoEm = :usadoEm where t.usuario = :usuario and t.usadoEm is null")
  int marcarTokensComoUsados(@Param("usuario") Usuario usuario, @Param("usadoEm") LocalDateTime usadoEm);
}
