package br.com.g3.contabilidade.repositoryimpl;

import br.com.g3.contabilidade.domain.ContaBancaria;
import br.com.g3.contabilidade.repository.ContaBancariaRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class ContaBancariaRepositoryImpl implements ContaBancariaRepository {
  private final ContaBancariaJpaRepository jpaRepository;

  public ContaBancariaRepositoryImpl(ContaBancariaJpaRepository jpaRepository) {
    this.jpaRepository = jpaRepository;
  }

  @Override
  public ContaBancaria salvar(ContaBancaria conta) {
    return jpaRepository.save(conta);
  }

  @Override
  public List<ContaBancaria> listar() {
    return jpaRepository.findAll();
  }

  @Override
  public Optional<ContaBancaria> buscarPorId(Long id) {
    return jpaRepository.findById(id);
  }

  @Override
  public Optional<ContaBancaria> buscarRecebimentoLocal() {
    return jpaRepository.findFirstByRecebimentoLocalTrue();
  }

  @Override
  public java.util.List<ContaBancaria> listarRecebimentoLocal() {
    return jpaRepository.findByRecebimentoLocalTrue();
  }

  @Override
  public void remover(Long id) {
    jpaRepository.deleteById(id);
  }
}
