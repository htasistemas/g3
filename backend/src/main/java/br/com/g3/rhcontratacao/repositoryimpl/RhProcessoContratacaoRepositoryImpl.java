package br.com.g3.rhcontratacao.repositoryimpl;

import br.com.g3.rhcontratacao.domain.RhProcessoContratacao;
import br.com.g3.rhcontratacao.repository.RhProcessoContratacaoRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class RhProcessoContratacaoRepositoryImpl implements RhProcessoContratacaoRepository {
  private final RhProcessoContratacaoJpaRepository jpaRepository;

  public RhProcessoContratacaoRepositoryImpl(RhProcessoContratacaoJpaRepository jpaRepository) {
    this.jpaRepository = jpaRepository;
  }

  @Override
  public RhProcessoContratacao salvar(RhProcessoContratacao processo) {
    return jpaRepository.save(processo);
  }

  @Override
  public Optional<RhProcessoContratacao> buscarPorId(Long id) {
    return jpaRepository.findById(id);
  }

  @Override
  public Optional<RhProcessoContratacao> buscarPorCandidatoId(Long candidatoId) {
    return jpaRepository.findByCandidatoId(candidatoId);
  }

  @Override
  public List<RhProcessoContratacao> listar() {
    return jpaRepository.findAllByOrderByAtualizadoEmDesc();
  }
}
