package br.com.g3.rhcontratacao.repositoryimpl;

import br.com.g3.rhcontratacao.domain.RhTermo;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RhTermoJpaRepository extends JpaRepository<RhTermo, Long> {
  List<RhTermo> findByProcessoIdOrderByAtualizadoEmDesc(Long processoId);
  Optional<RhTermo> findByProcessoIdAndTipo(Long processoId, String tipo);
}
