package br.com.g3.contabilidade.repositoryimpl;

import br.com.g3.contabilidade.domain.LancamentoFinanceiro;
import br.com.g3.contabilidade.repository.LancamentoFinanceiroRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class LancamentoFinanceiroRepositoryImpl implements LancamentoFinanceiroRepository {
  private final LancamentoFinanceiroJpaRepository jpaRepository;

  public LancamentoFinanceiroRepositoryImpl(LancamentoFinanceiroJpaRepository jpaRepository) {
    this.jpaRepository = jpaRepository;
  }

  @Override
  public LancamentoFinanceiro salvar(LancamentoFinanceiro lancamento) {
    return jpaRepository.save(lancamento);
  }

  @Override
  public List<LancamentoFinanceiro> listar() {
    return jpaRepository.findAll();
  }

  @Override
  public Optional<LancamentoFinanceiro> buscarPorId(Long id) {
    return jpaRepository.findById(id);
  }
}
