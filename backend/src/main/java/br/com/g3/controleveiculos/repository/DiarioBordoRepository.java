package br.com.g3.controleveiculos.repository;

import br.com.g3.controleveiculos.domain.DiarioBordo;
import java.util.List;
import java.util.Optional;

public interface DiarioBordoRepository {
  DiarioBordo salvar(DiarioBordo diario);

  Optional<DiarioBordo> buscarPorId(Long id);

  List<DiarioBordo> listar();

  void remover(DiarioBordo diario);
}
