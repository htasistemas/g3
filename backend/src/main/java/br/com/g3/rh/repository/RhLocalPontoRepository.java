package br.com.g3.rh.repository;

import br.com.g3.rh.domain.RhLocalPonto;
import java.util.List;
import java.util.Optional;

public interface RhLocalPontoRepository {
  List<RhLocalPonto> listar();
  List<RhLocalPonto> listarAtivos();
  Optional<RhLocalPonto> buscarPorId(Long id);
  Optional<RhLocalPonto> buscarPrimeiroAtivo();
  RhLocalPonto salvar(RhLocalPonto local);
  void remover(RhLocalPonto local);
}
