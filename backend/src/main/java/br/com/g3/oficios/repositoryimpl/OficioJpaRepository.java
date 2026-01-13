package br.com.g3.oficios.repositoryimpl;

import br.com.g3.oficios.domain.Oficio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OficioJpaRepository extends JpaRepository<Oficio, Long> {
  @Query("SELECT MAX(o.numero) FROM Oficio o WHERE o.numero LIKE CONCAT('%/', :ano)")
  String buscarUltimoNumeroPorAno(@Param("ano") String ano);
}
