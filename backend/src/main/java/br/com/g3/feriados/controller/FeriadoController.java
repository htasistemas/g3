package br.com.g3.feriados.controller;

import br.com.g3.feriados.dto.FeriadoRequest;
import br.com.g3.feriados.dto.FeriadoResponse;
import br.com.g3.feriados.service.FeriadoService;
import jakarta.validation.Valid;
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
@RequestMapping("/api/feriados")
public class FeriadoController {
  private final FeriadoService service;

  public FeriadoController(FeriadoService service) {
    this.service = service;
  }

  @GetMapping
  public ResponseEntity<List<FeriadoResponse>> listar() {
    return ResponseEntity.ok(service.listar());
  }

  @PostMapping
  public ResponseEntity<FeriadoResponse> criar(@Valid @RequestBody FeriadoRequest request) {
    return ResponseEntity.ok(service.criar(request));
  }

  @PutMapping("/{id}")
  public ResponseEntity<FeriadoResponse> atualizar(
      @PathVariable("id") Long id, @Valid @RequestBody FeriadoRequest request) {
    return ResponseEntity.ok(service.atualizar(id, request));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> excluir(@PathVariable("id") Long id) {
    service.excluir(id);
    return ResponseEntity.noContent().build();
  }
}
