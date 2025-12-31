package br.com.g3.autorizacaocompras.repositoryimpl;

import br.com.g3.autorizacaocompras.domain.AutorizacaoCompraReservaBancaria;
import br.com.g3.autorizacaocompras.repository.AutorizacaoCompraReservaBancariaRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class AutorizacaoCompraReservaBancariaRepositoryImpl
    implements AutorizacaoCompraReservaBancariaRepository {
  private final AutorizacaoCompraReservaBancariaJpaRepository jpaRepository;

  public AutorizacaoCompraReservaBancariaRepositoryImpl(
      AutorizacaoCompraReservaBancariaJpaRepository jpaRepository) {
    this.jpaRepository = jpaRepository;
  }

  @Override
  public AutorizacaoCompraReservaBancaria salvar(AutorizacaoCompraReservaBancaria reserva) {
    return jpaRepository.save(reserva);
  }

  @Override
  public List<AutorizacaoCompraReservaBancaria> listarPorCompra(Long compraId) {
    return jpaRepository.findByAutorizacaoCompraId(compraId);
  }

  @Override
  public Optional<AutorizacaoCompraReservaBancaria> buscarPorCompraEConta(
      Long compraId, Long contaId) {
    return jpaRepository.findByAutorizacaoCompraIdAndContaBancariaId(compraId, contaId);
  }

  @Override
  public void remover(AutorizacaoCompraReservaBancaria reserva) {
    jpaRepository.delete(reserva);
  }
}
