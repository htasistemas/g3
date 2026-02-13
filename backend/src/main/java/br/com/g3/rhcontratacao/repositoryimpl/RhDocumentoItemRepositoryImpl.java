package br.com.g3.rhcontratacao.repositoryimpl;

import br.com.g3.rhcontratacao.domain.RhDocumentoItem;
import br.com.g3.rhcontratacao.repository.RhDocumentoItemRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class RhDocumentoItemRepositoryImpl implements RhDocumentoItemRepository {
  private final RhDocumentoItemJpaRepository jpaRepository;

  public RhDocumentoItemRepositoryImpl(RhDocumentoItemJpaRepository jpaRepository) {
    this.jpaRepository = jpaRepository;
  }

  @Override
  public RhDocumentoItem salvar(RhDocumentoItem item) {
    return jpaRepository.save(item);
  }

  @Override
  public Optional<RhDocumentoItem> buscarPorId(Long id) {
    return jpaRepository.findById(id);
  }

  @Override
  public Optional<RhDocumentoItem> buscarPorProcessoETipo(Long processoId, String tipoDocumento) {
    return jpaRepository.findByProcessoIdAndTipoDocumento(processoId, tipoDocumento);
  }

  @Override
  public List<RhDocumentoItem> listarPorProcesso(Long processoId) {
    return jpaRepository.findByProcessoIdOrderByTipoDocumentoAsc(processoId);
  }
}
