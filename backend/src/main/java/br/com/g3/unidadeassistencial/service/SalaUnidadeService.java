package br.com.g3.unidadeassistencial.service;

import br.com.g3.unidadeassistencial.dto.SalaUnidadeRequest;
import br.com.g3.unidadeassistencial.dto.SalaUnidadeResponse;
import java.util.List;

public interface SalaUnidadeService {
  List<SalaUnidadeResponse> listar(Long unidadeId);

  SalaUnidadeResponse criar(SalaUnidadeRequest request);

  SalaUnidadeResponse atualizar(Long id, SalaUnidadeRequest request);

  void remover(Long id);
}
