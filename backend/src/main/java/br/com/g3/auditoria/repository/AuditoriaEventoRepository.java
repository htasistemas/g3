package br.com.g3.auditoria.repository;

import br.com.g3.auditoria.domain.AuditoriaEvento;
import java.util.Optional;
import java.util.UUID;

public interface AuditoriaEventoRepository {
  AuditoriaEvento salvar(AuditoriaEvento evento);

  Optional<AuditoriaEvento> buscarPorId(UUID id);
}
