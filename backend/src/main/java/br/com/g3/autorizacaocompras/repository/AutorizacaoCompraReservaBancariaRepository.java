package br.com.g3.autorizacaocompras.repository;

import br.com.g3.autorizacaocompras.domain.AutorizacaoCompraReservaBancaria;
import java.util.List;
import java.util.Optional;

public interface AutorizacaoCompraReservaBancariaRepository {
  AutorizacaoCompraReservaBancaria salvar(AutorizacaoCompraReservaBancaria reserva);

  List<AutorizacaoCompraReservaBancaria> listarPorCompra(Long compraId);

  Optional<AutorizacaoCompraReservaBancaria> buscarPorCompraEConta(Long compraId, Long contaId);

  void remover(AutorizacaoCompraReservaBancaria reserva);
}
