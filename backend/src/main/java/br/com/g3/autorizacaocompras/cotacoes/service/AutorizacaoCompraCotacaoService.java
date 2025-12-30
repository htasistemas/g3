package br.com.g3.autorizacaocompras.cotacoes.service;

import br.com.g3.autorizacaocompras.cotacoes.dto.AutorizacaoCompraCotacaoRequest;
import br.com.g3.autorizacaocompras.cotacoes.dto.AutorizacaoCompraCotacaoResponse;
import java.util.List;

public interface AutorizacaoCompraCotacaoService {
  List<AutorizacaoCompraCotacaoResponse> listarPorCompraId(Long compraId);

  AutorizacaoCompraCotacaoResponse criar(Long compraId, AutorizacaoCompraCotacaoRequest request);

  void remover(Long compraId, Long cotacaoId);
}
