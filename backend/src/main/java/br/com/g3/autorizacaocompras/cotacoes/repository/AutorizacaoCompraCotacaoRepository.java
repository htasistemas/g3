package br.com.g3.autorizacaocompras.cotacoes.repository;

import br.com.g3.autorizacaocompras.cotacoes.domain.AutorizacaoCompraCotacao;
import java.util.List;

public interface AutorizacaoCompraCotacaoRepository {
  AutorizacaoCompraCotacao salvar(AutorizacaoCompraCotacao cotacao);

  List<AutorizacaoCompraCotacao> listarPorCompraId(Long compraId);
}
