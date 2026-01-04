package br.com.g3.termofomento.repositoryimpl;

import br.com.g3.termofomento.domain.TermoFomentoAditivo;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

public interface TermoFomentoAditivoJpaRepository
    extends JpaRepository<TermoFomentoAditivo, Long> {
  List<TermoFomentoAditivo> findAllByTermoFomentoIdOrderByDataAditivoDesc(Long termoFomentoId);

  @Modifying
  @Transactional
  void deleteByTermoFomentoId(Long termoFomentoId);
}
