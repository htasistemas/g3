package br.com.g3.contabilidade.repositoryimpl;

import br.com.g3.contabilidade.domain.MovimentacaoFinanceira;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovimentacaoFinanceiraJpaRepository extends JpaRepository<MovimentacaoFinanceira, Long> {}
