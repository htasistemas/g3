package br.com.g3.unidadeassistencial.controller;

import br.com.g3.unidadeassistencial.dto.SalaUnidadeRequest;
import br.com.g3.unidadeassistencial.dto.SalaUnidadeResponse;
import br.com.g3.unidadeassistencial.service.SalaUnidadeService;
import jakarta.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/salas")
public class SalaUnidadeController {
  private final SalaUnidadeService service;

  public SalaUnidadeController(SalaUnidadeService service) {
    this.service = service;
  }

  @GetMapping
  public Map<String, List<SalaUnidadeResponse>> listar(
      @RequestParam(value = "unidadeId", required = false) Long unidadeId) {
    Map<String, List<SalaUnidadeResponse>> response = new HashMap<>();
    response.put("rooms", service.listar(unidadeId));
    return response;
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public Map<String, SalaUnidadeResponse> criar(@Valid @RequestBody SalaUnidadeRequest request) {
    Map<String, SalaUnidadeResponse> response = new HashMap<>();
    response.put("room", service.criar(request));
    return response;
  }

  @PutMapping("/{id}")
  public Map<String, SalaUnidadeResponse> atualizar(
      @PathVariable("id") Long id, @Valid @RequestBody SalaUnidadeRequest request) {
    Map<String, SalaUnidadeResponse> response = new HashMap<>();
    response.put("room", service.atualizar(id, request));
    return response;
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void remover(@PathVariable("id") Long id) {
    service.remover(id);
  }
}
