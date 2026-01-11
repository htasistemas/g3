package br.com.g3.emprestimoseventos.repositoryimpl;

import br.com.g3.emprestimoseventos.domain.EmprestimoEventoMovimentacao;
import br.com.g3.emprestimoseventos.repository.EmprestimoEventoMovimentacaoRepository;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class EmprestimoEventoMovimentacaoRepositoryImpl
    implements EmprestimoEventoMovimentacaoRepository {
  private final EmprestimoEventoMovimentacaoJpaRepository jpaRepository;

  public EmprestimoEventoMovimentacaoRepositoryImpl(
      EmprestimoEventoMovimentacaoJpaRepository jpaRepository) {
    this.jpaRepository = jpaRepository;
  }

  @Override
  public EmprestimoEventoMovimentacao salvar(EmprestimoEventoMovimentacao movimentacao) {
    return jpaRepository.save(movimentacao);
  }

  @Override
  public List<EmprestimoEventoMovimentacao> listarPorEmprestimoId(Long emprestimoId) {
    return jpaRepository.findByEmprestimoIdOrderByCriadoEmDesc(emprestimoId);
  }
}
