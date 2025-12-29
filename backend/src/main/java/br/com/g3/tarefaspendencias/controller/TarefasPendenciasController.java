package br.com.g3.tarefaspendencias.controller;

import br.com.g3.tarefaspendencias.dto.TarefaPendenciaRequest;
import br.com.g3.tarefaspendencias.dto.TarefaPendenciaResponse;
import br.com.g3.tarefaspendencias.service.TarefaPendenciaService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/administrativo/tarefas")
public class TarefasPendenciasController {
  private final TarefaPendenciaService service;

  public TarefasPendenciasController(TarefaPendenciaService service) {
    this.service = service;
  }

  @GetMapping
  public List<TarefaPendenciaResponse> listar() {
    return service.listar();
  }

  @GetMapping("/{id}")
  public TarefaPendenciaResponse buscar(@PathVariable Long id) {
    return service.buscarPorId(id);
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public TarefaPendenciaResponse criar(@Valid @RequestBody TarefaPendenciaRequest request) {
    return service.criar(request);
  }

  @PutMapping("/{id}")
  public TarefaPendenciaResponse atualizar(
      @PathVariable Long id, @Valid @RequestBody TarefaPendenciaRequest request) {
    return service.atualizar(id, request);
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void remover(@PathVariable Long id) {
    service.remover(id);
  }
}
