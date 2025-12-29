package br.com.g3.contabilidade.repositoryimpl;

import br.com.g3.contabilidade.domain.LancamentoFinanceiro;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LancamentoFinanceiroJpaRepository extends JpaRepository<LancamentoFinanceiro, Long> {}
