package br.com.g3.unidadeassistencial.repositoryimpl;

import br.com.g3.unidadeassistencial.domain.SalaUnidade;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SalaUnidadeJpaRepository extends JpaRepository<SalaUnidade, Long> {
  List<SalaUnidade> findByUnidadeAssistencialId(Long unidadeId);
}
