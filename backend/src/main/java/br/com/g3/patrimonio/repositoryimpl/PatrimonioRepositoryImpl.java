package br.com.g3.patrimonio.repositoryimpl;

import br.com.g3.patrimonio.domain.PatrimonioItem;
import br.com.g3.patrimonio.repository.PatrimonioRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class PatrimonioRepositoryImpl implements PatrimonioRepository {
  private final PatrimonioJpaRepository jpaRepository;

  public PatrimonioRepositoryImpl(PatrimonioJpaRepository jpaRepository) {
    this.jpaRepository = jpaRepository;
  }

  @Override
  public PatrimonioItem salvar(PatrimonioItem item) {
    return jpaRepository.save(item);
  }

  @Override
  public List<PatrimonioItem> listar() {
    return jpaRepository.findAll();
  }

  @Override
  public Optional<PatrimonioItem> buscarPorId(Long id) {
    return jpaRepository.findById(id);
  }
}
