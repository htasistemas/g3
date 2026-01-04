package br.com.g3.transparencia.controller;

import br.com.g3.transparencia.dto.TransparenciaListaResponse;
import br.com.g3.transparencia.dto.TransparenciaRequest;
import br.com.g3.transparencia.dto.TransparenciaResponse;
import br.com.g3.transparencia.service.TransparenciaService;
import jakarta.validation.Valid;
import java.util.HashMap;
import java.util.Map;
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
@RequestMapping("/api/transparencias")
public class TransparenciaController {
  private final TransparenciaService service;

  public TransparenciaController(TransparenciaService service) {
    this.service = service;
  }

  @GetMapping
  public ResponseEntity<TransparenciaListaResponse> listar() {
    return ResponseEntity.ok(service.listar());
  }

  @GetMapping("/{id}")
  public ResponseEntity<Map<String, TransparenciaResponse>> obter(@PathVariable("id") Long id) {
    Map<String, TransparenciaResponse> response = new HashMap<>();
    response.put("transparencia", service.obter(id));
    return ResponseEntity.ok(response);
  }

  @PostMapping
  public ResponseEntity<Map<String, TransparenciaResponse>> criar(
      @Valid @RequestBody TransparenciaRequest request) {
    Map<String, TransparenciaResponse> response = new HashMap<>();
    response.put("transparencia", service.criar(request));
    return ResponseEntity.ok(response);
  }

  @PutMapping("/{id}")
  public ResponseEntity<Map<String, TransparenciaResponse>> atualizar(
      @PathVariable("id") Long id, @Valid @RequestBody TransparenciaRequest request) {
    Map<String, TransparenciaResponse> response = new HashMap<>();
    response.put("transparencia", service.atualizar(id, request));
    return ResponseEntity.ok(response);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> excluir(@PathVariable("id") Long id) {
    service.excluir(id);
    return ResponseEntity.noContent().build();
  }
}
