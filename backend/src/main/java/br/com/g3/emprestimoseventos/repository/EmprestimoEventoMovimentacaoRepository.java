package br.com.g3.emprestimoseventos.repository;

import br.com.g3.emprestimoseventos.domain.EmprestimoEventoMovimentacao;
import java.util.List;

public interface EmprestimoEventoMovimentacaoRepository {
  EmprestimoEventoMovimentacao salvar(EmprestimoEventoMovimentacao movimentacao);

  List<EmprestimoEventoMovimentacao> listarPorEmprestimoId(Long emprestimoId);
}
