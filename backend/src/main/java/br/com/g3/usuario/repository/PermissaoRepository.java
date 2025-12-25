package br.com.g3.usuario.repository;

import br.com.g3.usuario.domain.Permissao;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PermissaoRepository extends JpaRepository<Permissao, Long> {
  Optional<Permissao> findByNomeIgnoreCase(String nome);
}
