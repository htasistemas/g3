package br.com.g3.rh.repositoryimpl;

import br.com.g3.rh.domain.RhPontoAuditoria;
import br.com.g3.rh.repository.RhPontoAuditoriaRepository;
import org.springframework.stereotype.Repository;

@Repository
public class RhPontoAuditoriaRepositoryImpl implements RhPontoAuditoriaRepository {
  private final RhPontoAuditoriaJpaRepository repository;

  public RhPontoAuditoriaRepositoryImpl(RhPontoAuditoriaJpaRepository repository) {
    this.repository = repository;
  }

  @Override
  public RhPontoAuditoria salvar(RhPontoAuditoria auditoria) {
    return repository.save(auditoria);
  }
}
