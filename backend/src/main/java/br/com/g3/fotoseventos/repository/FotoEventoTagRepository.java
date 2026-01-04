package br.com.g3.fotoseventos.repository;

import br.com.g3.fotoseventos.domain.FotoEventoTag;
import java.util.List;

public interface FotoEventoTagRepository {
  List<FotoEventoTag> listarPorEvento(Long eventoId);

  List<FotoEventoTag> salvarTodos(List<FotoEventoTag> tags);

  void removerPorEvento(Long eventoId);
}
