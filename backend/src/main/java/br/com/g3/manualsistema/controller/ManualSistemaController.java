package br.com.g3.manualsistema.controller;

import br.com.g3.manualsistema.dto.ManualSistemaMudancaListaResponse;
import br.com.g3.manualsistema.dto.ManualSistemaMudancaRequest;
import br.com.g3.manualsistema.dto.ManualSistemaMudancaResponse;
import br.com.g3.manualsistema.dto.ManualSistemaResumoResponse;
import br.com.g3.manualsistema.dto.ManualSistemaSecaoResponse;
import br.com.g3.manualsistema.service.ManualSistemaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/manual-sistema")
public class ManualSistemaController {
  private final ManualSistemaService service;

  public ManualSistemaController(ManualSistemaService service) {
    this.service = service;
  }

  @GetMapping
  public ResponseEntity<ManualSistemaResumoResponse> obterResumo() {
    return ResponseEntity.ok(service.obterResumo());
  }

  @GetMapping("/secoes/{slug}")
  public ResponseEntity<ManualSistemaSecaoResponse> obterSecao(@PathVariable String slug) {
    return ResponseEntity.ok(service.obterSecao(slug));
  }

  @GetMapping("/changelog")
  public ResponseEntity<ManualSistemaMudancaListaResponse> listarMudancas(
      @RequestParam(name = "limite", defaultValue = "10") int limite) {
    return ResponseEntity.ok(service.listarMudancas(limite));
  }

  @PostMapping("/mudancas")
  public ResponseEntity<ManualSistemaMudancaResponse> registrarMudanca(
      @RequestBody ManualSistemaMudancaRequest request) {
    return ResponseEntity.ok(service.registrarMudanca(request));
  }
}
