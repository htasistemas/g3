package br.com.g3.patrimonio.repositoryimpl;

import br.com.g3.patrimonio.domain.PatrimonioMovimentacao;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PatrimonioMovimentacaoJpaRepository
    extends JpaRepository<PatrimonioMovimentacao, Long> {}
