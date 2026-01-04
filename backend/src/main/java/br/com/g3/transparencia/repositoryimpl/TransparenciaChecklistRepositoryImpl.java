package br.com.g3.transparencia.repositoryimpl;

import br.com.g3.transparencia.domain.TransparenciaChecklist;
import br.com.g3.transparencia.repository.TransparenciaChecklistRepository;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class TransparenciaChecklistRepositoryImpl implements TransparenciaChecklistRepository {
  private final TransparenciaChecklistJpaRepository jpaRepository;

  public TransparenciaChecklistRepositoryImpl(
      TransparenciaChecklistJpaRepository jpaRepository) {
    this.jpaRepository = jpaRepository;
  }

  @Override
  public TransparenciaChecklist salvar(TransparenciaChecklist checklist) {
    return jpaRepository.save(checklist);
  }

  @Override
  public List<TransparenciaChecklist> listarPorTransparencia(Long transparenciaId) {
    return jpaRepository.findAllByTransparenciaId(transparenciaId);
  }

  @Override
  public void removerPorTransparencia(Long transparenciaId) {
    jpaRepository.deleteByTransparenciaId(transparenciaId);
  }
}
