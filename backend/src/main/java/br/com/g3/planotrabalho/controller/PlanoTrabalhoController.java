package br.com.g3.planotrabalho.controller;

import br.com.g3.planotrabalho.dto.PlanoTrabalhoListaResponse;
import br.com.g3.planotrabalho.dto.PlanoTrabalhoRequest;
import br.com.g3.planotrabalho.dto.PlanoTrabalhoResponse;
import br.com.g3.planotrabalho.service.PlanoTrabalhoService;
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
@RequestMapping("/api/planos-trabalho")
public class PlanoTrabalhoController {
  private final PlanoTrabalhoService service;

  public PlanoTrabalhoController(PlanoTrabalhoService service) {
    this.service = service;
  }

  @GetMapping
  public ResponseEntity<PlanoTrabalhoListaResponse> listar() {
    return ResponseEntity.ok(service.listar());
  }

  @GetMapping("/{id}")
  public ResponseEntity<Map<String, PlanoTrabalhoResponse>> obter(@PathVariable("id") Long id) {
    Map<String, PlanoTrabalhoResponse> response = new HashMap<>();
    response.put("plano", service.obter(id));
    return ResponseEntity.ok(response);
  }

  @PostMapping
  public ResponseEntity<Map<String, PlanoTrabalhoResponse>> criar(
      @Valid @RequestBody PlanoTrabalhoRequest request) {
    Map<String, PlanoTrabalhoResponse> response = new HashMap<>();
    response.put("plano", service.criar(request));
    return ResponseEntity.ok(response);
  }

  @PutMapping("/{id}")
  public ResponseEntity<Map<String, PlanoTrabalhoResponse>> atualizar(
      @PathVariable("id") Long id, @Valid @RequestBody PlanoTrabalhoRequest request) {
    Map<String, PlanoTrabalhoResponse> response = new HashMap<>();
    response.put("plano", service.atualizar(id, request));
    return ResponseEntity.ok(response);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> excluir(@PathVariable("id") Long id) {
    service.excluir(id);
    return ResponseEntity.noContent().build();
  }

  @GetMapping("/{id}/export")
  public ResponseEntity<PlanoTrabalhoResponse> exportar(@PathVariable("id") Long id) {
    return ResponseEntity.ok(service.obter(id));
  }
}
