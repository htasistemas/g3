package br.com.g3.contabilidade.repository;

import br.com.g3.contabilidade.domain.EmendaImpositiva;
import java.util.List;
import java.util.Optional;

public interface EmendaImpositivaRepository {
  EmendaImpositiva salvar(EmendaImpositiva emenda);

  List<EmendaImpositiva> listar();

  Optional<EmendaImpositiva> buscarPorId(Long id);
}
