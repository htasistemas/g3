package br.com.g3.rhcontratacao.repositoryimpl;

import br.com.g3.rhcontratacao.domain.RhPpd;
import br.com.g3.rhcontratacao.repository.RhPpdRepository;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class RhPpdRepositoryImpl implements RhPpdRepository {
  private final RhPpdJpaRepository jpaRepository;

  public RhPpdRepositoryImpl(RhPpdJpaRepository jpaRepository) {
    this.jpaRepository = jpaRepository;
  }

  @Override
  public RhPpd salvar(RhPpd ppd) {
    return jpaRepository.save(ppd);
  }

  @Override
  public Optional<RhPpd> buscarPorProcesso(Long processoId) {
    return jpaRepository.findByProcessoId(processoId);
  }
}
