package br.com.g3.manualsistema.repositoryimpl;

import br.com.g3.manualsistema.domain.ManualSistemaSecao;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ManualSistemaSecaoJpaRepository extends JpaRepository<ManualSistemaSecao, Long> {
  List<ManualSistemaSecao> findAllByOrderByOrdemAsc();

  Optional<ManualSistemaSecao> findBySlug(String slug);
}
