package br.com.g3.emprestimoseventos.repositoryimpl;

import br.com.g3.emprestimoseventos.domain.EmprestimoEventoItem;
import br.com.g3.emprestimoseventos.repository.EmprestimoEventoItemRepository;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class EmprestimoEventoItemRepositoryImpl implements EmprestimoEventoItemRepository {
  private final EmprestimoEventoItemJpaRepository jpaRepository;

  public EmprestimoEventoItemRepositoryImpl(EmprestimoEventoItemJpaRepository jpaRepository) {
    this.jpaRepository = jpaRepository;
  }

  @Override
  public List<EmprestimoEventoItem> salvarTodos(List<EmprestimoEventoItem> itens) {
    return jpaRepository.saveAll(itens);
  }

  @Override
  public List<EmprestimoEventoItem> listarPorEmprestimoId(Long emprestimoId) {
    return jpaRepository.findByEmprestimoId(emprestimoId);
  }

  @Override
  public void removerPorEmprestimoId(Long emprestimoId) {
    jpaRepository.deleteByEmprestimoId(emprestimoId);
  }
}
