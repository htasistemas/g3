package br.com.g3.termofomento.repositoryimpl;

import br.com.g3.termofomento.domain.TermoFomentoDocumento;
import br.com.g3.termofomento.repository.TermoFomentoDocumentoRepository;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class TermoFomentoDocumentoRepositoryImpl implements TermoFomentoDocumentoRepository {
  private final TermoFomentoDocumentoJpaRepository jpaRepository;

  public TermoFomentoDocumentoRepositoryImpl(TermoFomentoDocumentoJpaRepository jpaRepository) {
    this.jpaRepository = jpaRepository;
  }

  @Override
  public TermoFomentoDocumento salvar(TermoFomentoDocumento documento) {
    return jpaRepository.save(documento);
  }

  @Override
  public List<TermoFomentoDocumento> listarPorTermo(Long termoId) {
    return jpaRepository.findAllByTermoFomentoId(termoId);
  }

  @Override
  public List<TermoFomentoDocumento> listarPorAditivo(Long aditivoId) {
    return jpaRepository.findAllByAditivoId(aditivoId);
  }

  @Override
  public void removerPorTermo(Long termoId) {
    jpaRepository.deleteByTermoFomentoId(termoId);
  }
}
