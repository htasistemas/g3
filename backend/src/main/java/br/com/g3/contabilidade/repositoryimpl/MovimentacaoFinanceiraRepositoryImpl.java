package br.com.g3.contabilidade.repositoryimpl;

import br.com.g3.contabilidade.domain.MovimentacaoFinanceira;
import br.com.g3.contabilidade.repository.MovimentacaoFinanceiraRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class MovimentacaoFinanceiraRepositoryImpl implements MovimentacaoFinanceiraRepository {
  private final MovimentacaoFinanceiraJpaRepository jpaRepository;

  public MovimentacaoFinanceiraRepositoryImpl(MovimentacaoFinanceiraJpaRepository jpaRepository) {
    this.jpaRepository = jpaRepository;
  }

  @Override
  public MovimentacaoFinanceira salvar(MovimentacaoFinanceira movimentacao) {
    return jpaRepository.save(movimentacao);
  }

  @Override
  public List<MovimentacaoFinanceira> listar() {
    return jpaRepository.findAll();
  }

  @Override
  public Optional<MovimentacaoFinanceira> buscarPorId(Long id) {
    return jpaRepository.findById(id);
  }

  @Override
  public boolean existePorDoacaoId(Long doacaoId) {
    return doacaoId != null && jpaRepository.existsByDoacaoId(doacaoId);
  }

  @Override
  public void remover(Long id) {
    jpaRepository.deleteById(id);
  }
}
