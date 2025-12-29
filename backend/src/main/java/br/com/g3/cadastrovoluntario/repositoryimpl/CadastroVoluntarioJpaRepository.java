package br.com.g3.cadastrovoluntario.repositoryimpl;

import br.com.g3.cadastrovoluntario.domain.CadastroVoluntario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CadastroVoluntarioJpaRepository extends JpaRepository<CadastroVoluntario, Long> {}
