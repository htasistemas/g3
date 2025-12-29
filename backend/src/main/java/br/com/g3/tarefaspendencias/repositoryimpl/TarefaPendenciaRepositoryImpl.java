package br.com.g3.tarefaspendencias.repositoryimpl;

import br.com.g3.tarefaspendencias.domain.TarefaPendencia;
import br.com.g3.tarefaspendencias.repository.TarefaPendenciaRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class TarefaPendenciaRepositoryImpl implements TarefaPendenciaRepository {
  private final TarefasPendenciasJpaRepository jpaRepository;

  public TarefaPendenciaRepositoryImpl(TarefasPendenciasJpaRepository jpaRepository) {
    this.jpaRepository = jpaRepository;
  }

  @Override
  public TarefaPendencia salvar(TarefaPendencia tarefa) {
    return jpaRepository.save(tarefa);
  }

  @Override
  public List<TarefaPendencia> listar() {
    return jpaRepository.findAll();
  }

  @Override
  public Optional<TarefaPendencia> buscarPorId(Long id) {
    return jpaRepository.findById(id);
  }

  @Override
  public void remover(TarefaPendencia tarefa) {
    jpaRepository.delete(tarefa);
  }
}
