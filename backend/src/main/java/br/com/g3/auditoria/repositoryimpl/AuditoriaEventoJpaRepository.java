package br.com.g3.auditoria.repositoryimpl;

import br.com.g3.auditoria.domain.AuditoriaEvento;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuditoriaEventoJpaRepository extends JpaRepository<AuditoriaEvento, UUID> {}
