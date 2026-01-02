package br.com.g3.fotoseventos.repositoryimpl;

import br.com.g3.fotoseventos.domain.FotoEventoFoto;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FotoEventoFotoJpaRepository extends JpaRepository<FotoEventoFoto, Long> {
  List<FotoEventoFoto> findAllByEventoIdOrderByOrdemAscCriadoEmAsc(Long eventoId);

  Optional<FotoEventoFoto> findByIdAndEventoId(Long id, Long eventoId);

  long countByEventoId(Long eventoId);
}
