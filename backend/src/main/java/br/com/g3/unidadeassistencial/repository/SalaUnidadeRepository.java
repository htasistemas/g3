package br.com.g3.unidadeassistencial.repository;

import br.com.g3.unidadeassistencial.domain.SalaUnidade;
import java.util.List;

public interface SalaUnidadeRepository {
  List<SalaUnidade> listar();

  List<SalaUnidade> listarPorUnidade(Long unidadeId);

  SalaUnidade salvar(SalaUnidade sala);

  java.util.Optional<SalaUnidade> buscarPorId(Long id);

  void remover(SalaUnidade sala);
}
