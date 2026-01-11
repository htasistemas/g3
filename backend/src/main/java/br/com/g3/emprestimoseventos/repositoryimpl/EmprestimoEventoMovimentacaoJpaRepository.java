package br.com.g3.emprestimoseventos.repositoryimpl;

import br.com.g3.emprestimoseventos.domain.EmprestimoEventoMovimentacao;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmprestimoEventoMovimentacaoJpaRepository
    extends JpaRepository<EmprestimoEventoMovimentacao, Long> {
  List<EmprestimoEventoMovimentacao> findByEmprestimoIdOrderByCriadoEmDesc(Long emprestimoId);
}
