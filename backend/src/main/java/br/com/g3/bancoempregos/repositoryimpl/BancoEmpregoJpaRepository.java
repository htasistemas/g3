package br.com.g3.bancoempregos.repositoryimpl;

import br.com.g3.bancoempregos.domain.BancoEmprego;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BancoEmpregoJpaRepository extends JpaRepository<BancoEmprego, Long> {}
