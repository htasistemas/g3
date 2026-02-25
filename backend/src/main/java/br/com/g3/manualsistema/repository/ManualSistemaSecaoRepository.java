package br.com.g3.manualsistema.repository;

import br.com.g3.manualsistema.domain.ManualSistemaSecao;
import java.util.List;
import java.util.Optional;

public interface ManualSistemaSecaoRepository {
  List<ManualSistemaSecao> listarOrdenado();

  Optional<ManualSistemaSecao> buscarPorSlug(String slug);

  ManualSistemaSecao salvar(ManualSistemaSecao secao);
}
