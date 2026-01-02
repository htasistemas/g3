package br.com.g3.fotoseventos.repository;

import br.com.g3.fotoseventos.domain.FotoEventoFoto;
import java.util.List;
import java.util.Optional;

public interface FotoEventoFotoRepository {
  FotoEventoFoto salvar(FotoEventoFoto foto);

  List<FotoEventoFoto> listarPorEvento(Long eventoId);

  Optional<FotoEventoFoto> buscarPorId(Long id);

  Optional<FotoEventoFoto> buscarPorEvento(Long eventoId, Long fotoId);

  long contarPorEvento(Long eventoId);

  void remover(FotoEventoFoto foto);
}
