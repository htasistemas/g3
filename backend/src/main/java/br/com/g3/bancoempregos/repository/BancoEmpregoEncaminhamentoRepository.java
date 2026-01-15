package br.com.g3.bancoempregos.repository;

import br.com.g3.bancoempregos.domain.BancoEmpregoEncaminhamento;
import java.util.List;

public interface BancoEmpregoEncaminhamentoRepository {
  BancoEmpregoEncaminhamento salvar(BancoEmpregoEncaminhamento encaminhamento);

  List<BancoEmpregoEncaminhamento> listarPorEmpregoId(Long empregoId);

  void removerPorEmpregoId(Long empregoId);
}
