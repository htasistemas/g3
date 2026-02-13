package br.com.g3.rhcontratacao.repositoryimpl;

import br.com.g3.rhcontratacao.domain.RhCartaBanco;
import br.com.g3.rhcontratacao.repository.RhCartaBancoRepository;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class RhCartaBancoRepositoryImpl implements RhCartaBancoRepository {
  private final RhCartaBancoJpaRepository jpaRepository;

  public RhCartaBancoRepositoryImpl(RhCartaBancoJpaRepository jpaRepository) {
    this.jpaRepository = jpaRepository;
  }

  @Override
  public RhCartaBanco salvar(RhCartaBanco carta) {
    return jpaRepository.save(carta);
  }

  @Override
  public Optional<RhCartaBanco> buscarPorProcesso(Long processoId) {
    return jpaRepository.findByProcessoId(processoId);
  }
}
