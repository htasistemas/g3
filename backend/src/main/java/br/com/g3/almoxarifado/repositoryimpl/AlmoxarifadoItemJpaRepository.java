package br.com.g3.almoxarifado.repositoryimpl;

import br.com.g3.almoxarifado.domain.AlmoxarifadoItem;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AlmoxarifadoItemJpaRepository extends JpaRepository<AlmoxarifadoItem, Long> {
  Optional<AlmoxarifadoItem> findByCodigo(String codigo);

  Optional<AlmoxarifadoItem> findByCodigoBarras(String codigoBarras);

  @Query(
      "SELECT item FROM AlmoxarifadoItem item " +
      "WHERE lower(item.descricao) = lower(:descricao) " +
      "AND lower(item.categoria) = lower(:categoria) " +
      "AND lower(item.unidade) = lower(:unidade) " +
      "AND lower(coalesce(item.localizacao, '')) = lower(coalesce(:localizacao, '')) " +
      "AND lower(coalesce(item.localizacaoInterna, '')) = lower(coalesce(:localizacaoInterna, ''))")
  Optional<AlmoxarifadoItem> buscarDuplicado(
      @Param("descricao") String descricao,
      @Param("categoria") String categoria,
      @Param("unidade") String unidade,
      @Param("localizacao") String localizacao,
      @Param("localizacaoInterna") String localizacaoInterna);
}
