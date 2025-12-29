package br.com.g3.contabilidade.repositoryimpl;

import br.com.g3.contabilidade.domain.ContaBancaria;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContaBancariaJpaRepository extends JpaRepository<ContaBancaria, Long> {}
