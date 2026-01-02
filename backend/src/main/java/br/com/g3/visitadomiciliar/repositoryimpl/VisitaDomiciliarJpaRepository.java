package br.com.g3.visitadomiciliar.repositoryimpl;

import br.com.g3.visitadomiciliar.domain.VisitaDomiciliar;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VisitaDomiciliarJpaRepository extends JpaRepository<VisitaDomiciliar, Long> {
  List<VisitaDomiciliar> findAllByOrderByDataVisitaDesc();
}
