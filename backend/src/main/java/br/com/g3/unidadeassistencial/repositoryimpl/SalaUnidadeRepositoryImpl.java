package br.com.g3.unidadeassistencial.repositoryimpl;

import br.com.g3.unidadeassistencial.domain.SalaUnidade;
import br.com.g3.unidadeassistencial.repository.SalaUnidadeRepository;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class SalaUnidadeRepositoryImpl implements SalaUnidadeRepository {
  private final SalaUnidadeJpaRepository jpaRepository;

  public SalaUnidadeRepositoryImpl(SalaUnidadeJpaRepository jpaRepository) {
    this.jpaRepository = jpaRepository;
  }

  @Override
  public List<SalaUnidade> listar() {
    return jpaRepository.findAll();
  }

  @Override
  public List<SalaUnidade> listarPorUnidade(Long unidadeId) {
    return jpaRepository.findByUnidadeAssistencialId(unidadeId);
  }

  @Override
  public SalaUnidade salvar(SalaUnidade sala) {
    return jpaRepository.save(sala);
  }

  @Override
  public java.util.Optional<SalaUnidade> buscarPorId(Long id) {
    return jpaRepository.findById(id);
  }

  @Override
  public void remover(SalaUnidade sala) {
    jpaRepository.delete(sala);
  }
}
