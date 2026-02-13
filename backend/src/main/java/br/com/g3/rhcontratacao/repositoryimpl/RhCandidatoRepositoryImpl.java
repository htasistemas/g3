package br.com.g3.rhcontratacao.repositoryimpl;

import br.com.g3.rhcontratacao.domain.RhCandidato;
import br.com.g3.rhcontratacao.repository.RhCandidatoRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class RhCandidatoRepositoryImpl implements RhCandidatoRepository {
  private final RhCandidatoJpaRepository jpaRepository;

  public RhCandidatoRepositoryImpl(RhCandidatoJpaRepository jpaRepository) {
    this.jpaRepository = jpaRepository;
  }

  @Override
  public RhCandidato salvar(RhCandidato candidato) {
    return jpaRepository.save(candidato);
  }

  @Override
  public Optional<RhCandidato> buscarPorId(Long id) {
    return jpaRepository.findById(id);
  }

  @Override
  public List<RhCandidato> listar() {
    return jpaRepository.findAllByOrderByNomeCompletoAsc();
  }
}
