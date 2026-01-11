package br.com.g3.bancoempregos.repositoryimpl;

import br.com.g3.bancoempregos.domain.BancoEmpregoCandidato;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BancoEmpregoCandidatoJpaRepository
    extends JpaRepository<BancoEmpregoCandidato, Long> {
  List<BancoEmpregoCandidato> findByEmpregoId(Long empregoId);
}
