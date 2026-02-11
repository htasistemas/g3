package br.com.g3.rh.repositoryimpl;

import br.com.g3.rh.domain.RhConfiguracaoPonto;
import br.com.g3.rh.repository.RhConfiguracaoPontoRepository;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class RhConfiguracaoPontoRepositoryImpl implements RhConfiguracaoPontoRepository {
  private static final Long ID_PADRAO = 1L;
  private final RhConfiguracaoPontoJpaRepository repository;

  public RhConfiguracaoPontoRepositoryImpl(RhConfiguracaoPontoJpaRepository repository) {
    this.repository = repository;
  }

  @Override
  public Optional<RhConfiguracaoPonto> buscarAtual() {
    return repository.findById(ID_PADRAO);
  }

  @Override
  public RhConfiguracaoPonto salvar(RhConfiguracaoPonto configuracao) {
    if (configuracao.getId() == null) {
      configuracao.setId(ID_PADRAO);
    }
    return repository.save(configuracao);
  }
}
