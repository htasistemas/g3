package br.com.g3.cadastrovoluntario.repositoryimpl;

import br.com.g3.cadastrovoluntario.domain.CadastroVoluntario;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CadastroVoluntarioJpaRepository extends JpaRepository<CadastroVoluntario, Long> {
  List<CadastroVoluntario> findByNomeCompletoContainingIgnoreCase(String nomeCompleto);
}
