package br.com.g3.termofomento.repository;

import br.com.g3.termofomento.domain.TermoFomentoDocumento;
import java.util.List;

public interface TermoFomentoDocumentoRepository {
  TermoFomentoDocumento salvar(TermoFomentoDocumento documento);

  List<TermoFomentoDocumento> listarPorTermo(Long termoId);

  List<TermoFomentoDocumento> listarPorAditivo(Long aditivoId);

  void removerPorTermo(Long termoId);
}
