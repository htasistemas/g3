package br.com.g3.emprestimoseventos.repositoryimpl;

import br.com.g3.emprestimoseventos.domain.EventoEmprestimo;
import br.com.g3.emprestimoseventos.repository.EventoEmprestimoRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class EventoEmprestimoRepositoryImpl implements EventoEmprestimoRepository {
  private final EventoEmprestimoJpaRepository jpaRepository;

  public EventoEmprestimoRepositoryImpl(EventoEmprestimoJpaRepository jpaRepository) {
    this.jpaRepository = jpaRepository;
  }

  @Override
  public EventoEmprestimo salvar(EventoEmprestimo evento) {
    return jpaRepository.save(evento);
  }

  @Override
  public Optional<EventoEmprestimo> buscarPorId(Long id) {
    return jpaRepository.findById(id);
  }

  @Override
  public List<EventoEmprestimo> listar() {
    return jpaRepository.findAll();
  }

  @Override
  public void remover(EventoEmprestimo evento) {
    jpaRepository.delete(evento);
  }
}
