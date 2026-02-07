package br.com.g3.cadastrobeneficiario.repositoryimpl;

import br.com.g3.cadastrobeneficiario.domain.CadastroBeneficiario;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CadastroBeneficiarioJpaRepository extends JpaRepository<CadastroBeneficiario, Long> {
  List<CadastroBeneficiario> findByNomeCompletoContainingIgnoreCase(String nomeCompleto);

  List<CadastroBeneficiario> findByNomeCompletoContainingIgnoreCaseAndStatus(String nomeCompleto, String status);

  List<CadastroBeneficiario> findByCodigoIn(List<String> codigos);

  @Query(
      value =
          "SELECT MAX(cast(codigo AS INTEGER)) FROM cadastro_beneficiario WHERE codigo IS NOT NULL AND codigo ~ '^[0-9]+$'",
      nativeQuery = true)
  Integer buscarMaiorCodigo();
}
