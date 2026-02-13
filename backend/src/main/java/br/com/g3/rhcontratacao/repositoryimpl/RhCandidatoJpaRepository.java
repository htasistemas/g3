package br.com.g3.rhcontratacao.repositoryimpl;

import br.com.g3.rhcontratacao.domain.RhCandidato;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RhCandidatoJpaRepository extends JpaRepository<RhCandidato, Long> {
  List<RhCandidato> findAllByOrderByNomeCompletoAsc();
}
