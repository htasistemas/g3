package br.com.g3.rh.repositoryimpl;

import br.com.g3.rh.domain.RhPontoAuditoria;
import java.time.LocalDateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RhPontoAuditoriaJpaRepository extends JpaRepository<RhPontoAuditoria, Long> {
  @Query("""
      SELECT a
      FROM RhPontoAuditoria a
      WHERE (:funcionarioId IS NULL OR a.funcionarioId = :funcionarioId)
        AND (:unidadeId IS NULL OR a.unidadeId = :unidadeId)
        AND (:resultado IS NULL OR a.resultado = :resultado)
        AND (:inicio IS NULL OR a.dataHoraServidor >= :inicio)
        AND (:fim IS NULL OR a.dataHoraServidor <= :fim)
      """)
  Page<RhPontoAuditoria> buscarAuditoria(
      @Param("funcionarioId") Long funcionarioId,
      @Param("unidadeId") Long unidadeId,
      @Param("resultado") String resultado,
      @Param("inicio") LocalDateTime inicio,
      @Param("fim") LocalDateTime fim,
      Pageable pageable);
}
