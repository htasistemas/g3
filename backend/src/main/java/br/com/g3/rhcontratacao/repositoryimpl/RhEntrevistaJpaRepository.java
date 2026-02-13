package br.com.g3.rhcontratacao.repositoryimpl;

import br.com.g3.rhcontratacao.domain.RhEntrevista;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RhEntrevistaJpaRepository extends JpaRepository<RhEntrevista, Long> {
  List<RhEntrevista> findByProcessoIdOrderByDataEntrevistaDesc(Long processoId);
}
