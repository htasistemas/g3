package br.com.g3.fotoseventos.repositoryimpl;

import br.com.g3.fotoseventos.domain.FotoEventoTag;
import br.com.g3.fotoseventos.repository.FotoEventoTagRepository;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class FotoEventoTagRepositoryImpl implements FotoEventoTagRepository {
  private final FotoEventoTagJpaRepository jpaRepository;

  public FotoEventoTagRepositoryImpl(FotoEventoTagJpaRepository jpaRepository) {
    this.jpaRepository = jpaRepository;
  }

  @Override
  public List<FotoEventoTag> listarPorEvento(Long eventoId) {
    return jpaRepository.findAllByEventoId(eventoId);
  }

  @Override
  public List<FotoEventoTag> salvarTodos(List<FotoEventoTag> tags) {
    return jpaRepository.saveAll(tags);
  }

  @Override
  public void removerPorEvento(Long eventoId) {
    jpaRepository.deleteByEventoId(eventoId);
  }
}
