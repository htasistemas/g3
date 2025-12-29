package br.com.g3.recebimentodoacao.repository;

import br.com.g3.recebimentodoacao.domain.Doador;
import java.util.List;
import java.util.Optional;

public interface DoadorRepository {
  Doador salvar(Doador doador);

  List<Doador> listar();

  Optional<Doador> buscarPorId(Long id);
}
