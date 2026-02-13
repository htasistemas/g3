package br.com.g3.rhcontratacao.repositoryimpl;

import br.com.g3.rhcontratacao.domain.RhDocumentoItem;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RhDocumentoItemJpaRepository extends JpaRepository<RhDocumentoItem, Long> {
  List<RhDocumentoItem> findByProcessoIdOrderByTipoDocumentoAsc(Long processoId);
  Optional<RhDocumentoItem> findByProcessoIdAndTipoDocumento(Long processoId, String tipoDocumento);
}
