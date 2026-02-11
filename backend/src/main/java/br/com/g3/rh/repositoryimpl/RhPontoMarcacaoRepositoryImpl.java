package br.com.g3.rh.repositoryimpl;

import br.com.g3.rh.domain.RhPontoMarcacao;
import br.com.g3.rh.repository.RhPontoMarcacaoRepository;
import org.springframework.stereotype.Repository;

@Repository
public class RhPontoMarcacaoRepositoryImpl implements RhPontoMarcacaoRepository {
  private final RhPontoMarcacaoJpaRepository repository;

  public RhPontoMarcacaoRepositoryImpl(RhPontoMarcacaoJpaRepository repository) {
    this.repository = repository;
  }

  @Override
  public RhPontoMarcacao salvar(RhPontoMarcacao marcacao) {
    return repository.save(marcacao);
  }
}
