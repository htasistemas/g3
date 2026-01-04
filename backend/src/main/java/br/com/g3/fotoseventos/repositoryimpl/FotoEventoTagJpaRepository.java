package br.com.g3.fotoseventos.repositoryimpl;

import br.com.g3.fotoseventos.domain.FotoEventoTag;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

public interface FotoEventoTagJpaRepository extends JpaRepository<FotoEventoTag, Long> {
  List<FotoEventoTag> findAllByEventoId(Long eventoId);

  @Modifying
  @Transactional
  void deleteByEventoId(Long eventoId);
}
