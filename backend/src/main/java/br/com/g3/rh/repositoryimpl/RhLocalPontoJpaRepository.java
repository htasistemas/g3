package br.com.g3.rh.repositoryimpl;

import br.com.g3.rh.domain.RhLocalPonto;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RhLocalPontoJpaRepository extends JpaRepository<RhLocalPonto, Long> {
  List<RhLocalPonto> findAllByOrderByNomeAsc();
  List<RhLocalPonto> findAllByAtivoTrueOrderByNomeAsc();
  Optional<RhLocalPonto> findFirstByAtivoTrueOrderByIdAsc();
}
