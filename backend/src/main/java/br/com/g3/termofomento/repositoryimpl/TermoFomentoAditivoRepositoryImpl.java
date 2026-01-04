package br.com.g3.termofomento.repositoryimpl;

import br.com.g3.termofomento.domain.TermoFomentoAditivo;
import br.com.g3.termofomento.repository.TermoFomentoAditivoRepository;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class TermoFomentoAditivoRepositoryImpl implements TermoFomentoAditivoRepository {
  private final TermoFomentoAditivoJpaRepository jpaRepository;

  public TermoFomentoAditivoRepositoryImpl(TermoFomentoAditivoJpaRepository jpaRepository) {
    this.jpaRepository = jpaRepository;
  }

  @Override
  public TermoFomentoAditivo salvar(TermoFomentoAditivo aditivo) {
    return jpaRepository.save(aditivo);
  }

  @Override
  public List<TermoFomentoAditivo> listarPorTermo(Long termoId) {
    return jpaRepository.findAllByTermoFomentoIdOrderByDataAditivoDesc(termoId);
  }

  @Override
  public void removerPorTermo(Long termoId) {
    jpaRepository.deleteByTermoFomentoId(termoId);
  }
}
