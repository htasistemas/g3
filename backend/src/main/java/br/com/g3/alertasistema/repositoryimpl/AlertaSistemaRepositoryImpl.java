package br.com.g3.alertasistema.repositoryimpl;

import br.com.g3.alertasistema.domain.AlertaSistema;
import br.com.g3.alertasistema.repository.AlertaSistemaRepository;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class AlertaSistemaRepositoryImpl implements AlertaSistemaRepository {
  private final AlertaSistemaJpaRepository jpaRepository;

  public AlertaSistemaRepositoryImpl(AlertaSistemaJpaRepository jpaRepository) {
    this.jpaRepository = jpaRepository;
  }

  @Override
  public List<AlertaSistema> listar() {
    return jpaRepository.findAll();
  }

  @Override
  public AlertaSistema salvar(AlertaSistema alerta) {
    return jpaRepository.save(alerta);
  }

  @Override
  public void removerTodos() {
    jpaRepository.deleteAllInBatch();
  }
}
