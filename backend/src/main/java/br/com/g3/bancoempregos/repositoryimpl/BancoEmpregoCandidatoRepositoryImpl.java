package br.com.g3.bancoempregos.repositoryimpl;

import br.com.g3.bancoempregos.domain.BancoEmpregoCandidato;
import br.com.g3.bancoempregos.repository.BancoEmpregoCandidatoRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class BancoEmpregoCandidatoRepositoryImpl implements BancoEmpregoCandidatoRepository {
  private final BancoEmpregoCandidatoJpaRepository jpaRepository;

  public BancoEmpregoCandidatoRepositoryImpl(BancoEmpregoCandidatoJpaRepository jpaRepository) {
    this.jpaRepository = jpaRepository;
  }

  @Override
  public List<BancoEmpregoCandidato> listarPorEmprego(Long empregoId) {
    return jpaRepository.findByEmpregoId(empregoId);
  }

  @Override
  public BancoEmpregoCandidato salvar(BancoEmpregoCandidato candidato) {
    return jpaRepository.save(candidato);
  }

  @Override
  public Optional<BancoEmpregoCandidato> buscarPorId(Long id) {
    return jpaRepository.findById(id);
  }

  @Override
  public void remover(BancoEmpregoCandidato candidato) {
    jpaRepository.delete(candidato);
  }
}
