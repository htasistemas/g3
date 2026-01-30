package br.com.g3.senhas.repository;

import br.com.g3.senhas.domain.SenhaChamada;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SenhaChamadaRepository extends JpaRepository<SenhaChamada, UUID> {
  @Query(
      """
      SELECT c FROM SenhaChamada c
      ORDER BY c.dataHoraChamada DESC
      """)
  List<SenhaChamada> listarUltimas();

  @Query(
      """
      SELECT c FROM SenhaChamada c
      WHERE (:unidadeId IS NULL OR c.unidadeId = :unidadeId)
      ORDER BY c.dataHoraChamada DESC
      """)
  List<SenhaChamada> listarUltimasPorUnidade(@Param("unidadeId") Long unidadeId);

  @Query(
      value =
          """
          SELECT * FROM senha_chamada c
          WHERE (:unidadeId IS NULL OR c.unidade_id = :unidadeId)
          ORDER BY c.data_hora_chamada DESC
          LIMIT 1
          """,
      nativeQuery = true)
  SenhaChamada buscarUltimaPorUnidade(@Param("unidadeId") Long unidadeId);

  SenhaChamada findTopByFilaIdOrderByDataHoraChamadaDesc(Long filaId);
}
