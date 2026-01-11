package br.com.g3.emprestimoseventos.repository;

import br.com.g3.emprestimoseventos.domain.EmprestimoEventoItem;
import java.util.List;

public interface EmprestimoEventoItemRepository {
  List<EmprestimoEventoItem> salvarTodos(List<EmprestimoEventoItem> itens);

  List<EmprestimoEventoItem> listarPorEmprestimoId(Long emprestimoId);

  void removerPorEmprestimoId(Long emprestimoId);
}
