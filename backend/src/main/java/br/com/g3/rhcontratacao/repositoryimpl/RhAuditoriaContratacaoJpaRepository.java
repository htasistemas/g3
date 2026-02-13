package br.com.g3.rhcontratacao.repositoryimpl;

import br.com.g3.rhcontratacao.domain.RhAuditoriaContratacao;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RhAuditoriaContratacaoJpaRepository extends JpaRepository<RhAuditoriaContratacao, Long> {
  List<RhAuditoriaContratacao> findByProcessoIdOrderByCriadoEmDesc(Long processoId);
}
