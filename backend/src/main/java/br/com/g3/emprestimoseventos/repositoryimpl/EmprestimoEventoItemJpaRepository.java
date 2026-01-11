package br.com.g3.emprestimoseventos.repositoryimpl;

import br.com.g3.emprestimoseventos.domain.EmprestimoEventoItem;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmprestimoEventoItemJpaRepository extends JpaRepository<EmprestimoEventoItem, Long> {
  List<EmprestimoEventoItem> findByEmprestimoId(Long emprestimoId);

  void deleteByEmprestimoId(Long emprestimoId);
}
