package br.com.g3.transparencia.repositoryimpl;

import br.com.g3.transparencia.domain.Transparencia;
import br.com.g3.transparencia.repository.TransparenciaRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class TransparenciaRepositoryImpl implements TransparenciaRepository {
  private final TransparenciaJpaRepository jpaRepository;

  public TransparenciaRepositoryImpl(TransparenciaJpaRepository jpaRepository) {
    this.jpaRepository = jpaRepository;
  }

  @Override
  public Transparencia salvar(Transparencia transparencia) {
    return jpaRepository.save(transparencia);
  }

  @Override
  public Optional<Transparencia> buscarPorId(Long id) {
    return jpaRepository.findById(id);
  }

  @Override
  public List<Transparencia> listar() {
    return jpaRepository.findAll();
  }

  @Override
  public void remover(Transparencia transparencia) {
    jpaRepository.delete(transparencia);
  }
}
