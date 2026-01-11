package br.com.g3.alertasistema.controller;

import br.com.g3.alertasistema.dto.AlertaSistemaRequest;
import br.com.g3.alertasistema.dto.AlertaSistemaResponse;
import br.com.g3.alertasistema.service.AlertaSistemaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/alertas-sistema")
public class AlertaSistemaController {
  private final AlertaSistemaService service;

  public AlertaSistemaController(AlertaSistemaService service) {
    this.service = service;
  }

  @GetMapping
  public ResponseEntity<AlertaSistemaResponse> obterConfiguracao() {
    return ResponseEntity.ok(service.obterConfiguracao());
  }

  @PostMapping
  public ResponseEntity<AlertaSistemaResponse> salvarConfiguracao(
      @RequestBody AlertaSistemaRequest request) {
    return ResponseEntity.ok(service.salvarConfiguracao(request));
  }
}
