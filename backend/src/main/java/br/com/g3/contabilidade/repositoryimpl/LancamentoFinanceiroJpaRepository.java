package br.com.g3.contabilidade.repositoryimpl;

import br.com.g3.contabilidade.domain.LancamentoFinanceiro;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LancamentoFinanceiroJpaRepository extends JpaRepository<LancamentoFinanceiro, Long> {
  Optional<LancamentoFinanceiro> findByCompraId(Long compraId);
}
