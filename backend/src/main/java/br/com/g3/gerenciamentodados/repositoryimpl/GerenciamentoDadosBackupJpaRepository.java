package br.com.g3.gerenciamentodados.repositoryimpl;

import br.com.g3.gerenciamentodados.domain.GerenciamentoDadosBackup;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GerenciamentoDadosBackupJpaRepository
    extends JpaRepository<GerenciamentoDadosBackup, Long> {
  List<GerenciamentoDadosBackup> findAllByOrderByIniciadoEmDesc();
}
