package br.com.g3.termofomento.repository;

import br.com.g3.termofomento.domain.TermoFomento;
import java.util.List;
import java.util.Optional;

public interface TermoFomentoRepository {
  TermoFomento salvar(TermoFomento termo);

  Optional<TermoFomento> buscarPorId(Long id);

  List<TermoFomento> listar();

  void remover(TermoFomento termo);
}
