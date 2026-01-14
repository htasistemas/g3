package br.com.g3.gerenciamentodados.repositoryimpl;

import br.com.g3.gerenciamentodados.domain.GerenciamentoDadosBackup;
import br.com.g3.gerenciamentodados.repository.GerenciamentoDadosBackupRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class GerenciamentoDadosBackupRepositoryImpl implements GerenciamentoDadosBackupRepository {
  private final GerenciamentoDadosBackupJpaRepository jpaRepository;

  public GerenciamentoDadosBackupRepositoryImpl(GerenciamentoDadosBackupJpaRepository jpaRepository) {
    this.jpaRepository = jpaRepository;
  }

  @Override
  public List<GerenciamentoDadosBackup> listar() {
    return jpaRepository.findAllByOrderByIniciadoEmDesc();
  }

  @Override
  public Optional<GerenciamentoDadosBackup> buscarPorId(Long id) {
    return jpaRepository.findById(id);
  }

  @Override
  public GerenciamentoDadosBackup salvar(GerenciamentoDadosBackup backup) {
    return jpaRepository.save(backup);
  }
}
