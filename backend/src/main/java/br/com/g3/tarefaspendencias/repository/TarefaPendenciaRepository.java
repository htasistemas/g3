package br.com.g3.tarefaspendencias.repository;

import br.com.g3.tarefaspendencias.domain.TarefaPendencia;
import java.util.List;
import java.util.Optional;

public interface TarefaPendenciaRepository {
  TarefaPendencia salvar(TarefaPendencia tarefa);

  List<TarefaPendencia> listar();

  Optional<TarefaPendencia> buscarPorId(Long id);

  void remover(TarefaPendencia tarefa);
}
