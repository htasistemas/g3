package br.com.g3.autorizacaocompras.repositoryimpl;

import br.com.g3.autorizacaocompras.domain.AutorizacaoCompraReservaBancaria;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AutorizacaoCompraReservaBancariaJpaRepository
    extends JpaRepository<AutorizacaoCompraReservaBancaria, Long> {
  List<AutorizacaoCompraReservaBancaria> findByAutorizacaoCompraId(Long autorizacaoCompraId);

  Optional<AutorizacaoCompraReservaBancaria> findByAutorizacaoCompraIdAndContaBancariaId(
      Long autorizacaoCompraId, Long contaBancariaId);
}
