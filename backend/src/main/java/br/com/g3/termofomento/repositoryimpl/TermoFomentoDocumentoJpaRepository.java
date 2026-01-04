package br.com.g3.termofomento.repositoryimpl;

import br.com.g3.termofomento.domain.TermoFomentoDocumento;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

public interface TermoFomentoDocumentoJpaRepository
    extends JpaRepository<TermoFomentoDocumento, Long> {
  List<TermoFomentoDocumento> findAllByTermoFomentoId(Long termoFomentoId);

  List<TermoFomentoDocumento> findAllByAditivoId(Long aditivoId);

  @Modifying
  @Transactional
  void deleteByTermoFomentoId(Long termoFomentoId);
}
