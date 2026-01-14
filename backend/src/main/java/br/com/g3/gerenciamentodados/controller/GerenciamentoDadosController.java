package br.com.g3.gerenciamentodados.controller;

import br.com.g3.gerenciamentodados.dto.GerenciamentoDadosBackupRequest;
import br.com.g3.gerenciamentodados.dto.GerenciamentoDadosBackupResponse;
import br.com.g3.gerenciamentodados.dto.GerenciamentoDadosConfiguracaoRequest;
import br.com.g3.gerenciamentodados.dto.GerenciamentoDadosConfiguracaoResponse;
import br.com.g3.gerenciamentodados.dto.GerenciamentoDadosRestauracaoResponse;
import br.com.g3.gerenciamentodados.service.GerenciamentoDadosService;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/config/gerenciamento-dados")
public class GerenciamentoDadosController {
  private final GerenciamentoDadosService service;

  public GerenciamentoDadosController(GerenciamentoDadosService service) {
    this.service = service;
  }

  @GetMapping("/configuracao")
  public GerenciamentoDadosConfiguracaoResponse obterConfiguracao() {
    return service.obterConfiguracao();
  }

  @PutMapping("/configuracao")
  public GerenciamentoDadosConfiguracaoResponse salvarConfiguracao(
      @RequestBody GerenciamentoDadosConfiguracaoRequest request) {
    return service.salvarConfiguracao(request);
  }

  @GetMapping("/backups")
  public List<GerenciamentoDadosBackupResponse> listarBackups() {
    return service.listarBackups();
  }

  @PostMapping("/backups")
  public GerenciamentoDadosBackupResponse criarBackup(
      @RequestBody GerenciamentoDadosBackupRequest request) {
    return service.criarBackup(request);
  }

  @PostMapping("/backups/{backupId}/restaurar")
  public GerenciamentoDadosRestauracaoResponse restaurarBackup(@PathVariable Long backupId) {
    return service.restaurarBackup(backupId);
  }
}
