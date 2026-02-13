package br.com.g3.rhcontratacao.repositoryimpl;

import br.com.g3.rhcontratacao.domain.RhPpd;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RhPpdJpaRepository extends JpaRepository<RhPpd, Long> {
  Optional<RhPpd> findByProcessoId(Long processoId);
}
