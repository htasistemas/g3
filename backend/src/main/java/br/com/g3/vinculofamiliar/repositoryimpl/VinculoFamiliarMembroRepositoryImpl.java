package br.com.g3.vinculofamiliar.repositoryimpl;

import br.com.g3.vinculofamiliar.domain.VinculoFamiliarMembro;
import br.com.g3.vinculofamiliar.repository.VinculoFamiliarMembroRepository;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class VinculoFamiliarMembroRepositoryImpl implements VinculoFamiliarMembroRepository {
  private final VinculoFamiliarMembroJpaRepository jpaRepository;

  public VinculoFamiliarMembroRepositoryImpl(VinculoFamiliarMembroJpaRepository jpaRepository) {
    this.jpaRepository = jpaRepository;
  }

  @Override
  public VinculoFamiliarMembro salvar(VinculoFamiliarMembro membro) {
    return jpaRepository.save(membro);
  }

  @Override
  public Optional<VinculoFamiliarMembro> buscarPorId(Long id) {
    return jpaRepository.findById(id);
  }

  @Override
  public void remover(VinculoFamiliarMembro membro) {
    jpaRepository.delete(membro);
  }
}
