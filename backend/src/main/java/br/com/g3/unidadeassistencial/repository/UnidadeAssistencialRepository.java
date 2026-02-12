package br.com.g3.unidadeassistencial.repository;

import br.com.g3.unidadeassistencial.domain.UnidadeAssistencial;
import java.util.List;
import java.util.Optional;

public interface UnidadeAssistencialRepository {
  UnidadeAssistencial salvar(UnidadeAssistencial unidade);

  List<UnidadeAssistencial> listar();

  Optional<UnidadeAssistencial> buscarPorId(Long id);

  Optional<UnidadeAssistencial> buscarAtual();

  Optional<UnidadeAssistencial> buscarPrincipal();

  void limparUnidadePrincipal();

  void remover(UnidadeAssistencial unidade);
}
