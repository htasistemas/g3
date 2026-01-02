package br.com.g3.visitadomiciliar.repositoryimpl;

import br.com.g3.visitadomiciliar.domain.VisitaDomiciliar;
import br.com.g3.visitadomiciliar.repository.VisitaDomiciliarRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class VisitaDomiciliarRepositoryImpl implements VisitaDomiciliarRepository {
  private final VisitaDomiciliarJpaRepository jpaRepository;

  public VisitaDomiciliarRepositoryImpl(VisitaDomiciliarJpaRepository jpaRepository) {
    this.jpaRepository = jpaRepository;
  }

  @Override
  public VisitaDomiciliar salvar(VisitaDomiciliar visita) {
    return jpaRepository.save(visita);
  }

  @Override
  public Optional<VisitaDomiciliar> buscarPorId(Long id) {
    return jpaRepository.findById(id);
  }

  @Override
  public List<VisitaDomiciliar> listar() {
    return jpaRepository.findAllByOrderByDataVisitaDesc();
  }

  @Override
  public void remover(VisitaDomiciliar visita) {
    jpaRepository.delete(visita);
  }
}
