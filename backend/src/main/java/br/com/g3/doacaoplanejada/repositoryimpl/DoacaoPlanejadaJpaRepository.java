package br.com.g3.doacaoplanejada.repositoryimpl;

import br.com.g3.doacaoplanejada.domain.DoacaoPlanejada;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface DoacaoPlanejadaJpaRepository extends JpaRepository<DoacaoPlanejada, Long> {
  @Query("select d from DoacaoPlanejada d join fetch d.item where d.beneficiario.id = :beneficiarioId")
  List<DoacaoPlanejada> listarPorBeneficiarioComItem(@Param("beneficiarioId") Long beneficiarioId);

  @Query("select d from DoacaoPlanejada d join fetch d.item where d.vinculoFamiliar.id = :vinculoFamiliarId")
  List<DoacaoPlanejada> listarPorVinculoFamiliarComItem(@Param("vinculoFamiliarId") Long vinculoFamiliarId);

  @Query("select d from DoacaoPlanejada d join fetch d.item")
  List<DoacaoPlanejada> listarTodasComItem();

  @Query(
      "select d from DoacaoPlanejada d "
          + "left join fetch d.item "
          + "left join fetch d.beneficiario "
          + "left join fetch d.vinculoFamiliar "
          + "where d.status = :status")
  List<DoacaoPlanejada> listarPendentesComRelacionamentos(@Param("status") String status);
}
