package br.com.g3.vinculofamiliar.repositoryimpl;

import br.com.g3.vinculofamiliar.domain.VinculoFamiliar;
import br.com.g3.vinculofamiliar.repository.VinculoFamiliarRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class VinculoFamiliarRepositoryImpl implements VinculoFamiliarRepository {
  private final VinculoFamiliarJpaRepository jpaRepository;

  public VinculoFamiliarRepositoryImpl(VinculoFamiliarJpaRepository jpaRepository) {
    this.jpaRepository = jpaRepository;
  }

  @Override
  public VinculoFamiliar salvar(VinculoFamiliar vinculo) {
    return jpaRepository.save(vinculo);
  }

  @Override
  public List<VinculoFamiliar> listar() {
    return jpaRepository.findAll();
  }

  @Override
  public Optional<VinculoFamiliar> buscarPorId(Long id) {
    return jpaRepository.findById(id);
  }

  @Override
  public void remover(VinculoFamiliar vinculo) {
    jpaRepository.delete(vinculo);
  }
}
