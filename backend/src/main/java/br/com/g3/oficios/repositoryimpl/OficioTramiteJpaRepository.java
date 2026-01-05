package br.com.g3.oficios.repositoryimpl;

import br.com.g3.oficios.domain.OficioTramite;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OficioTramiteJpaRepository extends JpaRepository<OficioTramite, Long> {
  List<OficioTramite> findByOficioIdOrderByDataDescIdDesc(Long oficioId);

  @Modifying
  @Query("DELETE FROM OficioTramite t WHERE t.oficioId = :oficioId")
  void deleteByOficioId(@Param("oficioId") Long oficioId);
}
