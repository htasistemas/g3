package br.com.g3.oficios.controller;

import br.com.g3.oficios.dto.OficioListaResponse;
import br.com.g3.oficios.dto.OficioRequest;
import br.com.g3.oficios.dto.OficioResponse;
import br.com.g3.oficios.service.OficioService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/oficios")
public class OficioController {
  private final OficioService service;

  public OficioController(OficioService service) {
    this.service = service;
  }

  @GetMapping
  public ResponseEntity<OficioListaResponse> listar() {
    return ResponseEntity.ok(service.listar());
  }

  @GetMapping("/{id}")
  public ResponseEntity<OficioResponse> obter(@PathVariable("id") Long id) {
    return ResponseEntity.ok(service.obter(id));
  }

  @PostMapping
  public ResponseEntity<OficioResponse> criar(@Valid @RequestBody OficioRequest request) {
    return ResponseEntity.ok(service.criar(request));
  }

  @PutMapping("/{id}")
  public ResponseEntity<OficioResponse> atualizar(
      @PathVariable("id") Long id, @Valid @RequestBody OficioRequest request) {
    return ResponseEntity.ok(service.atualizar(id, request));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> excluir(@PathVariable("id") Long id) {
    service.excluir(id);
    return ResponseEntity.noContent().build();
  }
}
