package br.com.g3.rhcontratacao.repositoryimpl;

import br.com.g3.rhcontratacao.domain.RhFichaAdmissao;
import br.com.g3.rhcontratacao.repository.RhFichaAdmissaoRepository;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class RhFichaAdmissaoRepositoryImpl implements RhFichaAdmissaoRepository {
  private final RhFichaAdmissaoJpaRepository jpaRepository;

  public RhFichaAdmissaoRepositoryImpl(RhFichaAdmissaoJpaRepository jpaRepository) {
    this.jpaRepository = jpaRepository;
  }

  @Override
  public RhFichaAdmissao salvar(RhFichaAdmissao ficha) {
    return jpaRepository.save(ficha);
  }

  @Override
  public Optional<RhFichaAdmissao> buscarPorProcesso(Long processoId) {
    return jpaRepository.findByProcessoId(processoId);
  }
}
