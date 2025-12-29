package br.com.g3.prontuario.repositoryimpl;

import br.com.g3.prontuario.domain.ProntuarioRegistro;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProntuarioRegistroJpaRepository extends JpaRepository<ProntuarioRegistro, Long> {
  Optional<ProntuarioRegistro> findById(Long id);
}
