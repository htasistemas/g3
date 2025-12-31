package br.com.g3.patrimonio.repository;

import br.com.g3.patrimonio.domain.PatrimonioItem;
import java.util.List;
import java.util.Optional;

public interface PatrimonioRepository {
  PatrimonioItem salvar(PatrimonioItem item);

  List<PatrimonioItem> listar();

  Optional<PatrimonioItem> buscarPorId(Long id);
}
