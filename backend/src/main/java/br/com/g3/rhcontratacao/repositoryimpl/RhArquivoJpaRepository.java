package br.com.g3.rhcontratacao.repositoryimpl;

import br.com.g3.rhcontratacao.domain.RhArquivo;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RhArquivoJpaRepository extends JpaRepository<RhArquivo, Long> {
  List<RhArquivo> findByProcessoIdOrderByCriadoEmDesc(Long processoId);
}
