package br.com.g3.lembretesdiarios.controller;

import br.com.g3.lembretesdiarios.dto.LembreteDiarioAdiarRequest;
import br.com.g3.lembretesdiarios.dto.LembreteDiarioRequest;
import br.com.g3.lembretesdiarios.dto.LembreteDiarioResponse;
import br.com.g3.lembretesdiarios.service.LembreteDiarioService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/lembretes-diarios")
public class LembretesDiariosController {
  private final LembreteDiarioService service;

  public LembretesDiariosController(LembreteDiarioService service) {
    this.service = service;
  }

  @GetMapping
  public List<LembreteDiarioResponse> listar() {
    return service.listar();
  }

  @PostMapping
  public LembreteDiarioResponse criar(@Valid @RequestBody LembreteDiarioRequest request) {
    return service.criar(request);
  }

  @PutMapping("/{id}")
  public LembreteDiarioResponse atualizar(
      @PathVariable Long id,
      @Valid @RequestBody LembreteDiarioRequest request) {
    return service.atualizar(id, request);
  }

  @PatchMapping("/{id}/concluir")
  public LembreteDiarioResponse concluir(@PathVariable Long id) {
    return service.concluir(id);
  }

  @PatchMapping("/{id}/adiar")
  public LembreteDiarioResponse adiar(
      @PathVariable Long id,
      @Valid @RequestBody LembreteDiarioAdiarRequest request) {
    return service.adiar(id, request);
  }

  @DeleteMapping("/{id}")
  public void excluir(@PathVariable Long id) {
    service.excluir(id);
  }
}
