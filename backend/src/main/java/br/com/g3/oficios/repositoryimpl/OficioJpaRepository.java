package br.com.g3.oficios.repositoryimpl;

import br.com.g3.oficios.domain.Oficio;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OficioJpaRepository extends JpaRepository<Oficio, Long> {}
