package br.com.g3.autorizacaocompras.repositoryimpl;

import br.com.g3.autorizacaocompras.domain.AutorizacaoCompra;
import br.com.g3.autorizacaocompras.repository.AutorizacaoComprasRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class AutorizacaoComprasRepositoryImpl implements AutorizacaoComprasRepository {
  private final AutorizacaoComprasJpaRepository jpaRepository;

  public AutorizacaoComprasRepositoryImpl(AutorizacaoComprasJpaRepository jpaRepository) {
    this.jpaRepository = jpaRepository;
  }

  @Override
  public AutorizacaoCompra salvar(AutorizacaoCompra compra) {
    return jpaRepository.save(compra);
  }

  @Override
  public List<AutorizacaoCompra> listar() {
    return jpaRepository.findAll();
  }

  @Override
  public Optional<AutorizacaoCompra> buscarPorId(Long id) {
    return jpaRepository.findById(id);
  }

  @Override
  public void remover(AutorizacaoCompra compra) {
    jpaRepository.delete(compra);
  }
}
