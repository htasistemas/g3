package br.com.g3.cadastrobeneficiario.repositoryimpl;

import br.com.g3.cadastrobeneficiario.domain.CadastroBeneficiario;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CadastroBeneficiarioJpaRepository extends JpaRepository<CadastroBeneficiario, Long> {
  List<CadastroBeneficiario> findByNomeCompletoContainingIgnoreCase(String nomeCompleto);
}
