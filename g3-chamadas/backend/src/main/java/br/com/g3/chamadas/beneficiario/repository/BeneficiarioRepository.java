package br.com.g3.chamadas.beneficiario.repository;

import br.com.g3.chamadas.beneficiario.entity.BeneficiarioEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BeneficiarioRepository extends JpaRepository<BeneficiarioEntity, Long> {
  List<BeneficiarioEntity> findByAtivoTrueOrderByNomeBeneficiarioAsc();
}
