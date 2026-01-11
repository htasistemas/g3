package br.com.g3.alertasistema.repositoryimpl;

import br.com.g3.alertasistema.domain.AlertaSistema;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlertaSistemaJpaRepository extends JpaRepository<AlertaSistema, Long> {}
