package br.com.g3.visitadomiciliar.repositoryimpl;

import br.com.g3.visitadomiciliar.domain.VisitaDomiciliarAnexo;
import br.com.g3.visitadomiciliar.repository.VisitaDomiciliarAnexoRepository;
import java.util.Collections;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class VisitaDomiciliarAnexoRepositoryImpl implements VisitaDomiciliarAnexoRepository {
  private final VisitaDomiciliarAnexoJpaRepository jpaRepository;

  public VisitaDomiciliarAnexoRepositoryImpl(VisitaDomiciliarAnexoJpaRepository jpaRepository) {
    this.jpaRepository = jpaRepository;
  }

  @Override
  public VisitaDomiciliarAnexo salvar(VisitaDomiciliarAnexo anexo) {
    return jpaRepository.save(anexo);
  }

  @Override
  public void removerPorVisitaId(Long visitaId) {
    jpaRepository.deleteByVisitaId(visitaId);
  }

  @Override
  public List<VisitaDomiciliarAnexo> listarPorVisitaIds(List<Long> visitaIds) {
    if (visitaIds == null || visitaIds.isEmpty()) {
      return Collections.emptyList();
    }
    return jpaRepository.findByVisitaIdIn(visitaIds);
  }
}
