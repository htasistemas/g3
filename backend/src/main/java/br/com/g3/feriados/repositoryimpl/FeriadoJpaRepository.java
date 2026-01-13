package br.com.g3.feriados.repositoryimpl;

import br.com.g3.feriados.domain.Feriado;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FeriadoJpaRepository extends JpaRepository<Feriado, Long> {
  List<Feriado> findAllByOrderByDataAscIdAsc();
}
