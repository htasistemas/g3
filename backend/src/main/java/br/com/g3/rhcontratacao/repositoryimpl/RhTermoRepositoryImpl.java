package br.com.g3.rhcontratacao.repositoryimpl;

import br.com.g3.rhcontratacao.domain.RhTermo;
import br.com.g3.rhcontratacao.repository.RhTermoRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class RhTermoRepositoryImpl implements RhTermoRepository {
  private final RhTermoJpaRepository jpaRepository;

  public RhTermoRepositoryImpl(RhTermoJpaRepository jpaRepository) {
    this.jpaRepository = jpaRepository;
  }

  @Override
  public RhTermo salvar(RhTermo termo) {
    return jpaRepository.save(termo);
  }

  @Override
  public Optional<RhTermo> buscarPorId(Long id) {
    return jpaRepository.findById(id);
  }

  @Override
  public Optional<RhTermo> buscarPorProcessoETipo(Long processoId, String tipo) {
    return jpaRepository.findByProcessoIdAndTipo(processoId, tipo);
  }

  @Override
  public List<RhTermo> listarPorProcesso(Long processoId) {
    return jpaRepository.findByProcessoIdOrderByAtualizadoEmDesc(processoId);
  }
}
