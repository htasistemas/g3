package br.com.g3.emprestimoseventos.repository;

import br.com.g3.emprestimoseventos.domain.EventoEmprestimo;
import java.util.List;
import java.util.Optional;

public interface EventoEmprestimoRepository {
  EventoEmprestimo salvar(EventoEmprestimo evento);

  Optional<EventoEmprestimo> buscarPorId(Long id);

  List<EventoEmprestimo> listar();

  void remover(EventoEmprestimo evento);
}
