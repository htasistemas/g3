package br.com.g3.cadastrobeneficiario.repositoryimpl;

import br.com.g3.cadastrobeneficiario.domain.DocumentoBeneficiario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DocumentoBeneficiarioJpaRepository extends JpaRepository<DocumentoBeneficiario, Long> {}
