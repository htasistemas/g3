package br.com.g3.contabilidade.repository;

import br.com.g3.contabilidade.domain.LancamentoFinanceiro;
import java.util.List;
import java.util.Optional;

public interface LancamentoFinanceiroRepository {
  LancamentoFinanceiro salvar(LancamentoFinanceiro lancamento);

  List<LancamentoFinanceiro> listar();

  Optional<LancamentoFinanceiro> buscarPorId(Long id);

  Optional<LancamentoFinanceiro> buscarPorCompraId(Long compraId);
}
