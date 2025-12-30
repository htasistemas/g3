package br.com.g3.autorizacaocompras.cotacoes.repositoryimpl;

import br.com.g3.autorizacaocompras.cotacoes.domain.AutorizacaoCompraCotacao;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AutorizacaoCompraCotacaoJpaRepository
    extends JpaRepository<AutorizacaoCompraCotacao, Long> {
  List<AutorizacaoCompraCotacao> findByAutorizacaoCompraIdOrderByCriadoEmDesc(Long compraId);

  boolean existsByIdAndAutorizacaoCompraId(Long id, Long compraId);

  @Query(
      value =
          "select * from autorizacao_compras_cotacoes "
              + "where regexp_replace(cnpj, '\\\\D', '', 'g') = :cnpj "
              + "order by criado_em desc limit 1",
      nativeQuery = true)
  Optional<AutorizacaoCompraCotacao> findUltimaPorCnpj(@Param("cnpj") String cnpj);
}
