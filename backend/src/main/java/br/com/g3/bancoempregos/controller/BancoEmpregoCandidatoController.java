package br.com.g3.bancoempregos.controller;

import br.com.g3.bancoempregos.dto.BancoEmpregoCandidatoRequest;
import br.com.g3.bancoempregos.dto.BancoEmpregoCandidatoResponse;
import br.com.g3.bancoempregos.service.BancoEmpregoCandidatoService;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/banco-empregos")
public class BancoEmpregoCandidatoController {
  private final BancoEmpregoCandidatoService service;

  public BancoEmpregoCandidatoController(BancoEmpregoCandidatoService service) {
    this.service = service;
  }

  @GetMapping("/{empregoId}/candidatos")
  public ResponseEntity<List<BancoEmpregoCandidatoResponse>> listar(@PathVariable Long empregoId) {
    return ResponseEntity.ok(service.listar(empregoId));
  }

  @PostMapping("/{empregoId}/candidatos")
  public ResponseEntity<BancoEmpregoCandidatoResponse> criar(
      @PathVariable Long empregoId, @RequestBody BancoEmpregoCandidatoRequest request) {
    return ResponseEntity.ok(service.criar(empregoId, request));
  }

  @DeleteMapping("/candidatos/{id}")
  public ResponseEntity<Void> remover(@PathVariable Long id) {
    service.remover(id);
    return ResponseEntity.noContent().build();
  }
}
