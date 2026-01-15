package br.com.g3.bancoempregos.repositoryimpl;

import br.com.g3.bancoempregos.domain.BancoEmprego;
import br.com.g3.bancoempregos.repository.BancoEmpregoRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class BancoEmpregoRepositoryImpl implements BancoEmpregoRepository {
  private final BancoEmpregoJpaRepository jpaRepository;

  public BancoEmpregoRepositoryImpl(BancoEmpregoJpaRepository jpaRepository) {
    this.jpaRepository = jpaRepository;
  }

  @Override
  public BancoEmprego salvar(BancoEmprego emprego) {
    return jpaRepository.save(emprego);
  }

  @Override
  public List<BancoEmprego> listar() {
    return jpaRepository.findAll();
  }

  @Override
  public Optional<BancoEmprego> buscarPorId(Long id) {
    return jpaRepository.findById(id);
  }

  @Override
  public void remover(BancoEmprego emprego) {
    jpaRepository.delete(emprego);
  }
}
