package br.com.g3.informacoesadministrativas.repository;

import br.com.g3.informacoesadministrativas.domain.InformacaoAdministrativa;
import java.util.List;
import java.util.Optional;

public interface InformacaoAdministrativaRepository {
  InformacaoAdministrativa salvar(InformacaoAdministrativa info);

  Optional<InformacaoAdministrativa> buscarPorId(Long id);

  List<InformacaoAdministrativa> listar();

  void remover(InformacaoAdministrativa info);
}
