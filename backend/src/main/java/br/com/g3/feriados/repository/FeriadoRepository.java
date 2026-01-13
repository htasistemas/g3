package br.com.g3.feriados.repository;

import br.com.g3.feriados.domain.Feriado;
import java.util.List;
import java.util.Optional;

public interface FeriadoRepository {
  Feriado salvar(Feriado feriado);

  List<Feriado> listar();

  Optional<Feriado> buscarPorId(Long id);

  void remover(Feriado feriado);
}
