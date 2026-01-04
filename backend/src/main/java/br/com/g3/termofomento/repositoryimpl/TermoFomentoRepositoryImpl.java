package br.com.g3.termofomento.repositoryimpl;

import br.com.g3.termofomento.domain.TermoFomento;
import br.com.g3.termofomento.repository.TermoFomentoRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class TermoFomentoRepositoryImpl implements TermoFomentoRepository {
  private final TermoFomentoJpaRepository jpaRepository;

  public TermoFomentoRepositoryImpl(TermoFomentoJpaRepository jpaRepository) {
    this.jpaRepository = jpaRepository;
  }

  @Override
  public TermoFomento salvar(TermoFomento termo) {
    return jpaRepository.save(termo);
  }

  @Override
  public Optional<TermoFomento> buscarPorId(Long id) {
    return jpaRepository.findById(id);
  }

  @Override
  public List<TermoFomento> listar() {
    return jpaRepository.findAll();
  }

  @Override
  public void remover(TermoFomento termo) {
    jpaRepository.delete(termo);
  }
}
