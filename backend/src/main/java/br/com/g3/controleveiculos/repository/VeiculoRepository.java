package br.com.g3.controleveiculos.repository;

import br.com.g3.controleveiculos.domain.Veiculo;
import java.util.List;
import java.util.Optional;

public interface VeiculoRepository {
  Veiculo salvar(Veiculo veiculo);

  Optional<Veiculo> buscarPorId(Long id);

  Optional<Veiculo> buscarPorPlaca(String placa);

  List<Veiculo> listar();

  void remover(Veiculo veiculo);
}
