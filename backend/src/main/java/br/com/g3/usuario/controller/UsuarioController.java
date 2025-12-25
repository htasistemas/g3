package br.com.g3.usuario.controller;

import br.com.g3.usuario.dto.UsuarioAtualizacaoRequest;
import br.com.g3.usuario.dto.UsuarioCriacaoRequest;
import br.com.g3.usuario.dto.UsuarioResponse;
import br.com.g3.usuario.service.UsuarioService;
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
@RequestMapping("/api/usuarios")
public class UsuarioController {
  private final UsuarioService service;

  public UsuarioController(UsuarioService service) {
    this.service = service;
  }

  @GetMapping
  public List<UsuarioResponse> listar() {
    return service.listar();
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public UsuarioResponse criar(@Valid @RequestBody UsuarioCriacaoRequest request) {
    return service.criar(request);
  }

  @PutMapping("/{id}")
  public UsuarioResponse atualizar(
      @PathVariable("id") Long id, @Valid @RequestBody UsuarioAtualizacaoRequest request) {
    return service.atualizar(id, request);
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void remover(@PathVariable("id") Long id) {
    service.remover(id);
  }
}
