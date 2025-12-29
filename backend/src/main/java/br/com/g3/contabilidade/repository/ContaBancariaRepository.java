package br.com.g3.contabilidade.repository;

import br.com.g3.contabilidade.domain.ContaBancaria;
import java.util.List;
import java.util.Optional;

public interface ContaBancariaRepository {
  ContaBancaria salvar(ContaBancaria conta);

  List<ContaBancaria> listar();

  Optional<ContaBancaria> buscarPorId(Long id);

  void remover(Long id);
}
