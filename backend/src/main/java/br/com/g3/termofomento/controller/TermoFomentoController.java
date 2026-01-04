package br.com.g3.termofomento.controller;

import br.com.g3.termofomento.dto.TermoFomentoAditivoRequest;
import br.com.g3.termofomento.dto.TermoFomentoRequest;
import br.com.g3.termofomento.dto.TermoFomentoResponse;
import br.com.g3.termofomento.service.TermoFomentoService;
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
@RequestMapping("/api/termos-fomento")
public class TermoFomentoController {
  private final TermoFomentoService service;

  public TermoFomentoController(TermoFomentoService service) {
    this.service = service;
  }

  @GetMapping
  public ResponseEntity<List<TermoFomentoResponse>> listar() {
    return ResponseEntity.ok(service.listar());
  }

  @GetMapping("/{id}")
  public ResponseEntity<TermoFomentoResponse> obter(@PathVariable("id") Long id) {
    return ResponseEntity.ok(service.obter(id));
  }

  @PostMapping
  public ResponseEntity<TermoFomentoResponse> criar(@Valid @RequestBody TermoFomentoRequest request) {
    return ResponseEntity.ok(service.criar(request));
  }

  @PutMapping("/{id}")
  public ResponseEntity<TermoFomentoResponse> atualizar(
      @PathVariable("id") Long id, @Valid @RequestBody TermoFomentoRequest request) {
    return ResponseEntity.ok(service.atualizar(id, request));
  }

  @PostMapping("/{id}/aditivos")
  public ResponseEntity<TermoFomentoResponse> adicionarAditivo(
      @PathVariable("id") Long id, @Valid @RequestBody TermoFomentoAditivoRequest request) {
    return ResponseEntity.ok(service.adicionarAditivo(id, request));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> excluir(@PathVariable("id") Long id) {
    service.excluir(id);
    return ResponseEntity.noContent().build();
  }
}
