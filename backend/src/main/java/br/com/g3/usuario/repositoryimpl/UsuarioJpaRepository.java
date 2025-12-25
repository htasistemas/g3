package br.com.g3.usuario.repositoryimpl;

import br.com.g3.usuario.domain.Usuario;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioJpaRepository extends JpaRepository<Usuario, Long> {
  @EntityGraph(attributePaths = "permissoes")
  List<Usuario> findAll();

  @EntityGraph(attributePaths = "permissoes")
  Optional<Usuario> findById(Long id);

  @EntityGraph(attributePaths = "permissoes")
  Optional<Usuario> findTopByNomeUsuarioIgnoreCase(String nomeUsuario);
}
