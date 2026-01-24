package br.com.g3.controleveiculos.repositoryimpl;

import br.com.g3.controleveiculos.domain.DiarioBordo;
import br.com.g3.controleveiculos.repository.DiarioBordoRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class DiarioBordoRepositoryImpl implements DiarioBordoRepository {
  private final DiarioBordoJpaRepository repositorioJpa;

  public DiarioBordoRepositoryImpl(DiarioBordoJpaRepository repositorioJpa) {
    this.repositorioJpa = repositorioJpa;
  }

  @Override
  public DiarioBordo salvar(DiarioBordo diario) {
    return repositorioJpa.save(diario);
  }

  @Override
  public Optional<DiarioBordo> buscarPorId(Long id) {
    return repositorioJpa.findById(id);
  }

  @Override
  public List<DiarioBordo> listar() {
    return repositorioJpa.findAllByOrderByDataDescIdDesc();
  }

  @Override
  public void remover(DiarioBordo diario) {
    repositorioJpa.delete(diario);
  }
}
