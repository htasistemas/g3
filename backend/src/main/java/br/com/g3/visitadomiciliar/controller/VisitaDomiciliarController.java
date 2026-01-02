package br.com.g3.visitadomiciliar.controller;

import br.com.g3.visitadomiciliar.dto.VisitaDomiciliarListaResponse;
import br.com.g3.visitadomiciliar.dto.VisitaDomiciliarRequest;
import br.com.g3.visitadomiciliar.dto.VisitaDomiciliarResponse;
import br.com.g3.visitadomiciliar.service.VisitaDomiciliarService;
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
@RequestMapping("/api/visitas-domiciliares")
public class VisitaDomiciliarController {
  private final VisitaDomiciliarService service;

  public VisitaDomiciliarController(VisitaDomiciliarService service) {
    this.service = service;
  }

  @GetMapping
  public ResponseEntity<VisitaDomiciliarListaResponse> listar() {
    return ResponseEntity.ok(service.listar());
  }

  @PostMapping
  public ResponseEntity<VisitaDomiciliarResponse> criar(
      @Valid @RequestBody VisitaDomiciliarRequest request) {
    return ResponseEntity.ok(service.criar(request));
  }

  @PutMapping("/{id}")
  public ResponseEntity<VisitaDomiciliarResponse> atualizar(
      @PathVariable("id") Long id, @Valid @RequestBody VisitaDomiciliarRequest request) {
    return ResponseEntity.ok(service.atualizar(id, request));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> excluir(@PathVariable("id") Long id) {
    service.excluir(id);
    return ResponseEntity.noContent().build();
  }
}
