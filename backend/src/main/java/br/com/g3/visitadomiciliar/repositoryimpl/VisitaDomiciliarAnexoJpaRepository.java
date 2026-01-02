package br.com.g3.visitadomiciliar.repositoryimpl;

import br.com.g3.visitadomiciliar.domain.VisitaDomiciliarAnexo;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VisitaDomiciliarAnexoJpaRepository
    extends JpaRepository<VisitaDomiciliarAnexo, Long> {
  List<VisitaDomiciliarAnexo> findByVisitaIdIn(List<Long> visitaIds);

  void deleteByVisitaId(Long visitaId);
}
