package br.com.g3.gerenciamentodados.service;

import br.com.g3.gerenciamentodados.dto.GerenciamentoDadosBackupRequest;
import br.com.g3.gerenciamentodados.dto.GerenciamentoDadosBackupResponse;
import br.com.g3.gerenciamentodados.dto.GerenciamentoDadosConfiguracaoRequest;
import br.com.g3.gerenciamentodados.dto.GerenciamentoDadosConfiguracaoResponse;
import br.com.g3.gerenciamentodados.dto.GerenciamentoDadosRestauracaoResponse;
import java.util.List;

public interface GerenciamentoDadosService {
  GerenciamentoDadosConfiguracaoResponse obterConfiguracao();

  GerenciamentoDadosConfiguracaoResponse salvarConfiguracao(GerenciamentoDadosConfiguracaoRequest request);

  List<GerenciamentoDadosBackupResponse> listarBackups();

  GerenciamentoDadosBackupResponse criarBackup(GerenciamentoDadosBackupRequest request);

  GerenciamentoDadosRestauracaoResponse restaurarBackup(Long backupId);
}
