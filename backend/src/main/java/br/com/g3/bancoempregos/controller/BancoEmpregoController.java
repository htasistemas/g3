package br.com.g3.bancoempregos.controller;

import br.com.g3.bancoempregos.dto.BancoEmpregoRequest;
import br.com.g3.bancoempregos.dto.BancoEmpregoResponse;
import br.com.g3.bancoempregos.service.BancoEmpregoService;
import java.util.List;
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
@RequestMapping("/api/banco-empregos")
public class BancoEmpregoController {
  private final BancoEmpregoService service;

  public BancoEmpregoController(BancoEmpregoService service) {
    this.service = service;
  }

  @GetMapping
  public ResponseEntity<List<BancoEmpregoResponse>> listar() {
    return ResponseEntity.ok(service.listar());
  }

  @GetMapping("/{id}")
  public ResponseEntity<BancoEmpregoResponse> buscarPorId(@PathVariable("id") Long id) {
    return ResponseEntity.ok(service.buscarPorId(id));
  }

  @PostMapping
  public ResponseEntity<BancoEmpregoResponse> criar(@RequestBody BancoEmpregoRequest request) {
    return ResponseEntity.ok(service.criar(request));
  }

  @PutMapping("/{id}")
  public ResponseEntity<BancoEmpregoResponse> atualizar(
      @PathVariable("id") Long id, @RequestBody BancoEmpregoRequest request) {
    return ResponseEntity.ok(service.atualizar(id, request));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> remover(@PathVariable("id") Long id) {
    service.remover(id);
    return ResponseEntity.noContent().build();
  }
}
