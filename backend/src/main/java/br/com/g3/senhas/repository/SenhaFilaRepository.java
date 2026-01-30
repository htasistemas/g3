package br.com.g3.senhas.repository;

import br.com.g3.senhas.domain.SenhaFila;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SenhaFilaRepository extends JpaRepository<SenhaFila, Long> {
  @Query(
      """
      SELECT f FROM SenhaFila f
      WHERE f.status = :status
      ORDER BY f.prioridade DESC, f.dataHoraEntrada ASC
      """)
  List<SenhaFila> listarPorStatus(@Param("status") String status);

  @Query(
      """
      SELECT f FROM SenhaFila f
      WHERE f.status IN ('AGUARDANDO', 'CHAMADO')
      ORDER BY f.prioridade DESC, f.dataHoraEntrada ASC
      """)
  List<SenhaFila> listarAtivas();

  @Query(
      """
      SELECT f FROM SenhaFila f
      WHERE f.status = :status
        AND (:unidadeId IS NULL OR f.unidadeId = :unidadeId)
      ORDER BY f.prioridade DESC, f.dataHoraEntrada ASC
      """)
  List<SenhaFila> listarPorStatusEUnidade(@Param("status") String status, @Param("unidadeId") Long unidadeId);

  @Query(
      """
      SELECT f FROM SenhaFila f
      WHERE f.status IN ('AGUARDANDO', 'CHAMADO')
        AND (:unidadeId IS NULL OR f.unidadeId = :unidadeId)
      ORDER BY f.prioridade DESC, f.dataHoraEntrada ASC
      """)
  List<SenhaFila> listarAtivasPorUnidade(@Param("unidadeId") Long unidadeId);
}
