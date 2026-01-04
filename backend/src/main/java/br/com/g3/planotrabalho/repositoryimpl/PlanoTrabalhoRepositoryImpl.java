package br.com.g3.planotrabalho.repositoryimpl;

import br.com.g3.planotrabalho.domain.PlanoTrabalho;
import br.com.g3.planotrabalho.repository.PlanoTrabalhoRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class PlanoTrabalhoRepositoryImpl implements PlanoTrabalhoRepository {
  private final PlanoTrabalhoJpaRepository jpaRepository;

  public PlanoTrabalhoRepositoryImpl(PlanoTrabalhoJpaRepository jpaRepository) {
    this.jpaRepository = jpaRepository;
  }

  @Override
  public PlanoTrabalho salvar(PlanoTrabalho plano) {
    return jpaRepository.save(plano);
  }

  @Override
  public Optional<PlanoTrabalho> buscarPorId(Long id) {
    return jpaRepository.findById(id);
  }

  @Override
  public List<PlanoTrabalho> listar() {
    return jpaRepository.findAll();
  }

  @Override
  public void remover(PlanoTrabalho plano) {
    jpaRepository.delete(plano);
  }
}
