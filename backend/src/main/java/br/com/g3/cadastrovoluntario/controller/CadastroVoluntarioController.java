package br.com.g3.cadastrovoluntario.controller;

import br.com.g3.cadastrovoluntario.dto.CadastroVoluntarioConsultaResponse;
import br.com.g3.cadastrovoluntario.dto.CadastroVoluntarioCriacaoRequest;
import br.com.g3.cadastrovoluntario.dto.CadastroVoluntarioListaResponse;
import br.com.g3.cadastrovoluntario.dto.CadastroVoluntarioResponse;
import br.com.g3.cadastrovoluntario.service.CadastroVoluntarioService;
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
@RequestMapping("/api/voluntarios")
public class CadastroVoluntarioController {
  private final CadastroVoluntarioService service;

  public CadastroVoluntarioController(CadastroVoluntarioService service) {
    this.service = service;
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public CadastroVoluntarioConsultaResponse criar(
      @Valid @RequestBody CadastroVoluntarioCriacaoRequest request) {
    CadastroVoluntarioResponse response = service.criar(request);
    return new CadastroVoluntarioConsultaResponse(response);
  }

  @PutMapping("/{id}")
  public CadastroVoluntarioConsultaResponse atualizar(
      @PathVariable("id") Long id, @Valid @RequestBody CadastroVoluntarioCriacaoRequest request) {
    CadastroVoluntarioResponse response = service.atualizar(id, request);
    return new CadastroVoluntarioConsultaResponse(response);
  }

  @GetMapping("/{id}")
  public CadastroVoluntarioConsultaResponse buscarPorId(@PathVariable("id") Long id) {
    return new CadastroVoluntarioConsultaResponse(service.buscarPorId(id));
  }

  @GetMapping
  public CadastroVoluntarioListaResponse listar() {
    List<CadastroVoluntarioResponse> voluntarios = service.listar();
    return new CadastroVoluntarioListaResponse(voluntarios);
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void remover(@PathVariable("id") Long id) {
    service.remover(id);
  }
}
