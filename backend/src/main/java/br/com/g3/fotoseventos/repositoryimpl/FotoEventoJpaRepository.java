package br.com.g3.fotoseventos.repositoryimpl;

import br.com.g3.fotoseventos.domain.FotoEvento;
import br.com.g3.fotoseventos.domain.StatusFotoEvento;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FotoEventoJpaRepository extends JpaRepository<FotoEvento, Long> {
  @Query(
      "select e from FotoEvento e "
          + "where (:unidadeId is null or e.unidadeId = :unidadeId) "
          + "and (:status is null or e.status = :status) "
          + "and (:busca = '' "
          + "or e.titulo like concat('%', cast(:busca as string), '%') "
          + "or e.descricao like concat('%', cast(:busca as string), '%') "
          + "or e.local like concat('%', cast(:busca as string), '%')) "
          + "and (:dataInicio is null or e.dataEvento >= :dataInicio) "
          + "and (:dataFim is null or e.dataEvento <= :dataFim) "
          + "and (:tags is null or exists (select 1 from FotoEventoTag t where t.eventoId = e.id and t.tag in :tags))")
  Page<FotoEvento> buscarComFiltros(
      @Param("busca") String busca,
      @Param("dataInicio") LocalDate dataInicio,
      @Param("dataFim") LocalDate dataFim,
      @Param("unidadeId") Long unidadeId,
      @Param("status") StatusFotoEvento status,
      @Param("tags") List<String> tags,
      Pageable pageable);

  @Query(
      value =
          "select e from FotoEvento e "
              + "left join FotoEventoFoto f on f.eventoId = e.id "
              + "where (:unidadeId is null or e.unidadeId = :unidadeId) "
              + "and (:status is null or e.status = :status) "
              + "and (:busca = '' "
              + "or e.titulo like concat('%', cast(:busca as string), '%') "
              + "or e.descricao like concat('%', cast(:busca as string), '%') "
              + "or e.local like concat('%', cast(:busca as string), '%')) "
              + "and (:dataInicio is null or e.dataEvento >= :dataInicio) "
              + "and (:dataFim is null or e.dataEvento <= :dataFim) "
              + "and (:tags is null or exists (select 1 from FotoEventoTag t where t.eventoId = e.id and t.tag in :tags)) "
              + "group by e "
              + "order by count(f) desc",
      countQuery =
          "select count(e) from FotoEvento e "
              + "where (:unidadeId is null or e.unidadeId = :unidadeId) "
              + "and (:status is null or e.status = :status) "
              + "and (:busca = '' "
              + "or e.titulo like concat('%', cast(:busca as string), '%') "
              + "or e.descricao like concat('%', cast(:busca as string), '%') "
              + "or e.local like concat('%', cast(:busca as string), '%')) "
              + "and (:dataInicio is null or e.dataEvento >= :dataInicio) "
              + "and (:dataFim is null or e.dataEvento <= :dataFim) "
              + "and (:tags is null or exists (select 1 from FotoEventoTag t where t.eventoId = e.id and t.tag in :tags))")
  Page<FotoEvento> buscarComFiltrosOrdenadoPorFotos(
      @Param("busca") String busca,
      @Param("dataInicio") LocalDate dataInicio,
      @Param("dataFim") LocalDate dataFim,
      @Param("unidadeId") Long unidadeId,
      @Param("status") StatusFotoEvento status,
      @Param("tags") List<String> tags,
      Pageable pageable);
}
