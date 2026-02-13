package br.com.g3.rhcontratacao.repositoryimpl;

import br.com.g3.rhcontratacao.domain.RhEntrevista;
import br.com.g3.rhcontratacao.repository.RhEntrevistaRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class RhEntrevistaRepositoryImpl implements RhEntrevistaRepository {
  private final RhEntrevistaJpaRepository jpaRepository;

  public RhEntrevistaRepositoryImpl(RhEntrevistaJpaRepository jpaRepository) {
    this.jpaRepository = jpaRepository;
  }

  @Override
  public RhEntrevista salvar(RhEntrevista entrevista) {
    return jpaRepository.save(entrevista);
  }

  @Override
  public Optional<RhEntrevista> buscarPorId(Long id) {
    return jpaRepository.findById(id);
  }

  @Override
  public List<RhEntrevista> listarPorProcesso(Long processoId) {
    return jpaRepository.findByProcessoIdOrderByDataEntrevistaDesc(processoId);
  }
}
