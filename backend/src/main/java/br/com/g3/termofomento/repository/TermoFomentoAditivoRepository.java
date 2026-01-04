package br.com.g3.termofomento.repository;

import br.com.g3.termofomento.domain.TermoFomentoAditivo;
import java.util.List;

public interface TermoFomentoAditivoRepository {
  TermoFomentoAditivo salvar(TermoFomentoAditivo aditivo);

  List<TermoFomentoAditivo> listarPorTermo(Long termoId);

  void removerPorTermo(Long termoId);
}
