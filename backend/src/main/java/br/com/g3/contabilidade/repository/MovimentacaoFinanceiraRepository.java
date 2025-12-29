package br.com.g3.contabilidade.repository;

import br.com.g3.contabilidade.domain.MovimentacaoFinanceira;
import java.util.List;
import java.util.Optional;

public interface MovimentacaoFinanceiraRepository {
  MovimentacaoFinanceira salvar(MovimentacaoFinanceira movimentacao);

  List<MovimentacaoFinanceira> listar();

  Optional<MovimentacaoFinanceira> buscarPorId(Long id);
}
