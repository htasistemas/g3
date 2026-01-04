package br.com.g3.transparencia.repositoryimpl;

import br.com.g3.transparencia.domain.TransparenciaChecklist;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

public interface TransparenciaChecklistJpaRepository
    extends JpaRepository<TransparenciaChecklist, Long> {
  List<TransparenciaChecklist> findAllByTransparenciaId(Long transparenciaId);

  @Modifying
  @Transactional
  void deleteByTransparenciaId(Long transparenciaId);
}
