package br.com.g3.rh.repositoryimpl;

import br.com.g3.rh.domain.RhLocalPonto;
import br.com.g3.rh.repository.RhLocalPontoRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class RhLocalPontoRepositoryImpl implements RhLocalPontoRepository {
  private final RhLocalPontoJpaRepository repository;

  public RhLocalPontoRepositoryImpl(RhLocalPontoJpaRepository repository) {
    this.repository = repository;
  }

  @Override
  public List<RhLocalPonto> listar() {
    return repository.findAllByOrderByNomeAsc();
  }

  @Override
  public List<RhLocalPonto> listarAtivos() {
    return repository.findAllByAtivoTrueOrderByNomeAsc();
  }

  @Override
  public Optional<RhLocalPonto> buscarPorId(Long id) {
    return repository.findById(id);
  }

  @Override
  public Optional<RhLocalPonto> buscarPrimeiroAtivo() {
    return repository.findFirstByAtivoTrueOrderByIdAsc();
  }

  @Override
  public RhLocalPonto salvar(RhLocalPonto local) {
    return repository.save(local);
  }

  @Override
  public void remover(RhLocalPonto local) {
    repository.delete(local);
  }
}
