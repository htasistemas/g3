package br.com.g3.autorizacaocompras.service;

import br.com.g3.autorizacaocompras.dto.AutorizacaoCompraRequest;
import br.com.g3.autorizacaocompras.dto.AutorizacaoCompraResponse;
import br.com.g3.autorizacaocompras.dto.AutorizacaoCompraReservaBancariaRequest;
import br.com.g3.autorizacaocompras.dto.AutorizacaoCompraReservaBancariaResponse;
import br.com.g3.autorizacaocompras.dto.AutorizacaoPagamentoRequest;
import java.util.List;

public interface AutorizacaoComprasService {
  List<AutorizacaoCompraResponse> listar();

  AutorizacaoCompraResponse buscarPorId(Long id);

  AutorizacaoCompraResponse criar(AutorizacaoCompraRequest request);

  AutorizacaoCompraResponse atualizar(Long id, AutorizacaoCompraRequest request);

  void remover(Long id);

  AutorizacaoCompraReservaBancariaResponse registrarReservaBancaria(
      Long id, AutorizacaoCompraReservaBancariaRequest request);

  void removerReservaBancaria(Long id, Long contaBancariaId);

  AutorizacaoCompraResponse gerarAutorizacaoPagamento(Long id, AutorizacaoPagamentoRequest request);
}
