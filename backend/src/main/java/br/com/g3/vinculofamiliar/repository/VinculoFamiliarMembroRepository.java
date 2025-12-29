package br.com.g3.vinculofamiliar.repository;

import br.com.g3.vinculofamiliar.domain.VinculoFamiliarMembro;
import java.util.Optional;

public interface VinculoFamiliarMembroRepository {
  VinculoFamiliarMembro salvar(VinculoFamiliarMembro membro);

  Optional<VinculoFamiliarMembro> buscarPorId(Long id);

  void remover(VinculoFamiliarMembro membro);
}
