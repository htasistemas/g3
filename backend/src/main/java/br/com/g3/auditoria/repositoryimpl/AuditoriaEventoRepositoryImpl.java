package br.com.g3.auditoria.repositoryimpl;

import br.com.g3.auditoria.domain.AuditoriaEvento;
import br.com.g3.auditoria.repository.AuditoriaEventoRepository;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Repository;

@Repository
public class AuditoriaEventoRepositoryImpl implements AuditoriaEventoRepository {
  private final AuditoriaEventoJpaRepository jpaRepository;

  public AuditoriaEventoRepositoryImpl(AuditoriaEventoJpaRepository jpaRepository) {
    this.jpaRepository = jpaRepository;
  }

  @Override
  public AuditoriaEvento salvar(AuditoriaEvento evento) {
    return jpaRepository.save(evento);
  }

  @Override
  public Optional<AuditoriaEvento> buscarPorId(UUID id) {
    return jpaRepository.findById(id);
  }
}
