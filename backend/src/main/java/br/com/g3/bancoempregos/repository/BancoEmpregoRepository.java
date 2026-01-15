package br.com.g3.bancoempregos.repository;

import br.com.g3.bancoempregos.domain.BancoEmprego;
import java.util.List;
import java.util.Optional;

public interface BancoEmpregoRepository {
  BancoEmprego salvar(BancoEmprego emprego);

  List<BancoEmprego> listar();

  Optional<BancoEmprego> buscarPorId(Long id);

  void remover(BancoEmprego emprego);
}
