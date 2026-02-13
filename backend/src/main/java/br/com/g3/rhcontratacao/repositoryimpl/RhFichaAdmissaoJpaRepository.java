package br.com.g3.rhcontratacao.repositoryimpl;

import br.com.g3.rhcontratacao.domain.RhFichaAdmissao;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RhFichaAdmissaoJpaRepository extends JpaRepository<RhFichaAdmissao, Long> {
  Optional<RhFichaAdmissao> findByProcessoId(Long processoId);
}
