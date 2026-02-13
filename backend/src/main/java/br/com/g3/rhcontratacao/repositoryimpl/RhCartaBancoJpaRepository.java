package br.com.g3.rhcontratacao.repositoryimpl;

import br.com.g3.rhcontratacao.domain.RhCartaBanco;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RhCartaBancoJpaRepository extends JpaRepository<RhCartaBanco, Long> {
  Optional<RhCartaBanco> findByProcessoId(Long processoId);
}
