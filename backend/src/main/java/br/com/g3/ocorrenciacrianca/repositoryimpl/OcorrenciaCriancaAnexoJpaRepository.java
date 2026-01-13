package br.com.g3.ocorrenciacrianca.repositoryimpl;

import br.com.g3.ocorrenciacrianca.domain.OcorrenciaCriancaAnexo;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OcorrenciaCriancaAnexoJpaRepository
    extends JpaRepository<OcorrenciaCriancaAnexo, Long> {
  List<OcorrenciaCriancaAnexo> findByOcorrenciaIdOrderByOrdemAscIdAsc(Long ocorrenciaId);
}
