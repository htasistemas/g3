package br.com.g3.gerenciamentodados.repository;

import br.com.g3.gerenciamentodados.domain.GerenciamentoDadosBackup;
import java.util.List;
import java.util.Optional;

public interface GerenciamentoDadosBackupRepository {
  List<GerenciamentoDadosBackup> listar();

  Optional<GerenciamentoDadosBackup> buscarPorId(Long id);

  GerenciamentoDadosBackup salvar(GerenciamentoDadosBackup backup);
}
