package br.com.g3.vinculofamiliar.repository;

import br.com.g3.vinculofamiliar.domain.VinculoFamiliar;
import java.util.List;
import java.util.Optional;

public interface VinculoFamiliarRepository {
  VinculoFamiliar salvar(VinculoFamiliar vinculo);

  List<VinculoFamiliar> listar();

  Optional<VinculoFamiliar> buscarPorId(Long id);

  void remover(VinculoFamiliar vinculo);
}
