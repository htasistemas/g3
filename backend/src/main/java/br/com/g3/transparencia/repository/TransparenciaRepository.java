package br.com.g3.transparencia.repository;

import br.com.g3.transparencia.domain.Transparencia;
import java.util.List;
import java.util.Optional;

public interface TransparenciaRepository {
  Transparencia salvar(Transparencia transparencia);

  Optional<Transparencia> buscarPorId(Long id);

  List<Transparencia> listar();

  void remover(Transparencia transparencia);
}
