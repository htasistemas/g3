package br.com.g3.doacaorealizada.repositoryimpl;

import br.com.g3.doacaorealizada.domain.DoacaoRealizada;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface DoacaoRealizadaJpaRepository extends JpaRepository<DoacaoRealizada, Long> {
  @Query(
      "select distinct d from DoacaoRealizada d "
          + "left join fetch d.itens itens "
          + "left join fetch itens.item "
          + "left join fetch d.beneficiario "
          + "where d.beneficiario.id = :beneficiarioId")
  List<DoacaoRealizada> listarPorBeneficiario(@Param("beneficiarioId") Long beneficiarioId);
}
