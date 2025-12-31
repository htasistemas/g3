package br.com.g3.patrimonio.repositoryimpl;

import br.com.g3.patrimonio.domain.PatrimonioMovimentacao;
import br.com.g3.patrimonio.repository.PatrimonioMovimentacaoRepository;
import org.springframework.stereotype.Repository;

@Repository
public class PatrimonioMovimentacaoRepositoryImpl implements PatrimonioMovimentacaoRepository {
  private final PatrimonioMovimentacaoJpaRepository jpaRepository;

  public PatrimonioMovimentacaoRepositoryImpl(
      PatrimonioMovimentacaoJpaRepository jpaRepository) {
    this.jpaRepository = jpaRepository;
  }

  @Override
  public PatrimonioMovimentacao salvar(PatrimonioMovimentacao movimento) {
    return jpaRepository.save(movimento);
  }
}
