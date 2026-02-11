package br.com.g3.unidadeassistencial.controller;

import br.com.g3.unidadeassistencial.dto.UnidadeAssistencialCriacaoRequest;
import br.com.g3.unidadeassistencial.dto.UnidadeAssistencialConsultaResponse;
import br.com.g3.unidadeassistencial.dto.UnidadeAssistencialResponse;
import br.com.g3.unidadeassistencial.service.UnidadeAssistencialService;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/unidades-assistenciais")
public class UnidadeAssistencialController {
  private final UnidadeAssistencialService service;

  public UnidadeAssistencialController(UnidadeAssistencialService service) {
    this.service = service;
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public UnidadeAssistencialResponse criar(@Valid @RequestBody UnidadeAssistencialCriacaoRequest request) {
    return service.criar(request);
  }

  @GetMapping
  public List<UnidadeAssistencialResponse> listar() {
    return service.listar();
  }

  @GetMapping("/atual")
  public UnidadeAssistencialConsultaResponse obterAtual() {
    return new UnidadeAssistencialConsultaResponse(service.obterAtual());
  }

  @PostMapping("/{id}/geocodificar-endereco")
  public UnidadeAssistencialResponse geocodificarEndereco(
      @PathVariable("id") Long id,
      @RequestParam(value = "forcar", required = false, defaultValue = "false") boolean forcar) {
    return service.geocodificarEndereco(id, forcar);
  }

  @PutMapping("/{id}")
  public UnidadeAssistencialResponse atualizar(
      @PathVariable("id") Long id, @Valid @RequestBody UnidadeAssistencialCriacaoRequest request) {
    return service.atualizar(id, request);
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void remover(@PathVariable("id") Long id) {
    service.remover(id);
  }
}
