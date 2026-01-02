package br.com.g3.recebimentodoacao.repositoryimpl;

import br.com.g3.recebimentodoacao.domain.Doador;
import br.com.g3.recebimentodoacao.repository.DoadorRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class DoadorRepositoryImpl implements DoadorRepository {
  private final DoadorJpaRepository jpaRepository;

  public DoadorRepositoryImpl(DoadorJpaRepository jpaRepository) {
    this.jpaRepository = jpaRepository;
  }

  @Override
  public Doador salvar(Doador doador) {
    return jpaRepository.save(doador);
  }

  @Override
  public List<Doador> listar() {
    return jpaRepository.findAll();
  }

  @Override
  public Optional<Doador> buscarPorId(Long id) {
    return jpaRepository.findById(id);
  }

  @Override
  public void remover(Doador doador) {
    jpaRepository.delete(doador);
  }
}
