package br.com.g3.autorizacaocompras.repositoryimpl;

import br.com.g3.autorizacaocompras.domain.AutorizacaoCompra;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AutorizacaoComprasJpaRepository extends JpaRepository<AutorizacaoCompra, Long> {}
