package br.com.g3.contabilidade.repositoryimpl;

import br.com.g3.contabilidade.domain.ContaBancaria;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContaBancariaJpaRepository extends JpaRepository<ContaBancaria, Long> {
  Optional<ContaBancaria> findFirstByRecebimentoLocalTrue();

  java.util.List<ContaBancaria> findByRecebimentoLocalTrue();
}
