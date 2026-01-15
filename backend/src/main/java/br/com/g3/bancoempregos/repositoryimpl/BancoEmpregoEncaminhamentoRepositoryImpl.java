package br.com.g3.bancoempregos.repositoryimpl;

import br.com.g3.bancoempregos.domain.BancoEmpregoEncaminhamento;
import br.com.g3.bancoempregos.repository.BancoEmpregoEncaminhamentoRepository;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class BancoEmpregoEncaminhamentoRepositoryImpl
    implements BancoEmpregoEncaminhamentoRepository {
  private final BancoEmpregoEncaminhamentoJpaRepository jpaRepository;

  public BancoEmpregoEncaminhamentoRepositoryImpl(
      BancoEmpregoEncaminhamentoJpaRepository jpaRepository) {
    this.jpaRepository = jpaRepository;
  }

  @Override
  public BancoEmpregoEncaminhamento salvar(BancoEmpregoEncaminhamento encaminhamento) {
    return jpaRepository.save(encaminhamento);
  }

  @Override
  public List<BancoEmpregoEncaminhamento> listarPorEmpregoId(Long empregoId) {
    return jpaRepository.findByEmpregoId(empregoId);
  }

  @Override
  public void removerPorEmpregoId(Long empregoId) {
    jpaRepository.deleteByEmpregoId(empregoId);
  }
}
