package br.com.g3.fotoseventos.repositoryimpl;

import br.com.g3.fotoseventos.domain.FotoEvento;
import java.time.LocalDate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FotoEventoJpaRepository extends JpaRepository<FotoEvento, Long> {
  @Query(
      "select e from FotoEvento e "
          + "where (:unidadeId is null or e.unidadeId = :unidadeId) "
          + "and (:busca is null "
          + "or lower(e.titulo) like lower(concat('%', :busca, '%')) "
          + "or lower(e.descricao) like lower(concat('%', :busca, '%')) "
          + "or lower(e.tags) like lower(concat('%', :busca, '%'))) "
          + "and (:dataInicio is null or e.dataEvento >= :dataInicio) "
          + "and (:dataFim is null or e.dataEvento <= :dataFim)")
  Page<FotoEvento> buscarComFiltros(
      @Param("busca") String busca,
      @Param("dataInicio") LocalDate dataInicio,
      @Param("dataFim") LocalDate dataFim,
      @Param("unidadeId") Long unidadeId,
      Pageable pageable);
}
