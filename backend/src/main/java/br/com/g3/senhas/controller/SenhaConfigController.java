package br.com.g3.senhas.controller;

import br.com.g3.senhas.dto.SenhaConfigRequest;
import br.com.g3.senhas.dto.SenhaConfigResponse;
import br.com.g3.senhas.service.SenhaConfigService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/senhas/config")
public class SenhaConfigController {
  private final SenhaConfigService service;

  public SenhaConfigController(SenhaConfigService service) {
    this.service = service;
  }

  @GetMapping
  public ResponseEntity<SenhaConfigResponse> obter() {
    return ResponseEntity.ok(service.obterConfiguracao());
  }

  @PutMapping
  public ResponseEntity<SenhaConfigResponse> atualizar(@Valid @RequestBody SenhaConfigRequest request) {
    return ResponseEntity.ok(service.atualizarConfiguracao(request));
  }
}
