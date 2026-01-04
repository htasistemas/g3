package br.com.g3.transparencia.repositoryimpl;

import br.com.g3.transparencia.domain.Transparencia;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransparenciaJpaRepository extends JpaRepository<Transparencia, Long> {}
