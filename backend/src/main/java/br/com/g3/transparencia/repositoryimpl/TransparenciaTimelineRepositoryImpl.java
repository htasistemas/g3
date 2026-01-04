package br.com.g3.transparencia.repositoryimpl;

import br.com.g3.transparencia.domain.TransparenciaTimeline;
import br.com.g3.transparencia.repository.TransparenciaTimelineRepository;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class TransparenciaTimelineRepositoryImpl implements TransparenciaTimelineRepository {
  private final TransparenciaTimelineJpaRepository jpaRepository;

  public TransparenciaTimelineRepositoryImpl(
      TransparenciaTimelineJpaRepository jpaRepository) {
    this.jpaRepository = jpaRepository;
  }

  @Override
  public TransparenciaTimeline salvar(TransparenciaTimeline timeline) {
    return jpaRepository.save(timeline);
  }

  @Override
  public List<TransparenciaTimeline> listarPorTransparencia(Long transparenciaId) {
    return jpaRepository.findAllByTransparenciaId(transparenciaId);
  }

  @Override
  public void removerPorTransparencia(Long transparenciaId) {
    jpaRepository.deleteByTransparenciaId(transparenciaId);
  }
}
