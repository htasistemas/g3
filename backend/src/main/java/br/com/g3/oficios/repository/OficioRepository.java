package br.com.g3.oficios.repository;

import br.com.g3.oficios.domain.Oficio;
import java.util.List;
import java.util.Optional;

public interface OficioRepository {
  Oficio salvar(Oficio oficio);

  Optional<Oficio> buscarPorId(Long id);

  List<Oficio> listar();

  void remover(Oficio oficio);
}
