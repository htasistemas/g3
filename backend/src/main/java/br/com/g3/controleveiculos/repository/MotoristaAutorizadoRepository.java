package br.com.g3.controleveiculos.repository;

import br.com.g3.controleveiculos.domain.MotoristaAutorizado;
import java.util.List;
import java.util.Optional;

public interface MotoristaAutorizadoRepository {
  MotoristaAutorizado salvar(MotoristaAutorizado motorista);

  Optional<MotoristaAutorizado> buscarPorId(Long id);

  Optional<MotoristaAutorizado> buscarPorDados(
      Long veiculoId, String tipoOrigem, Long profissionalId, Long voluntarioId);

  List<MotoristaAutorizado> listar();

  List<MotoristaAutorizado> listarPorVeiculo(Long veiculoId);

  void remover(MotoristaAutorizado motorista);
}
