package br.com.g3.autorizacaocompras.controller;

import br.com.g3.autorizacaocompras.dto.AutorizacaoCompraRequest;
import br.com.g3.autorizacaocompras.dto.AutorizacaoCompraResponse;
import br.com.g3.autorizacaocompras.service.AutorizacaoComprasService;
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
@RequestMapping("/api/financeiro/autorizacao-compras")
public class AutorizacaoComprasController {
  private final AutorizacaoComprasService service;

  public AutorizacaoComprasController(AutorizacaoComprasService service) {
    this.service = service;
  }

  @GetMapping
  public List<AutorizacaoCompraResponse> listar() {
    return service.listar();
  }

  @GetMapping("/{id}")
  public AutorizacaoCompraResponse buscar(@PathVariable("id") Long id) {
    return service.buscarPorId(id);
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public AutorizacaoCompraResponse criar(@Valid @RequestBody AutorizacaoCompraRequest request) {
    return service.criar(request);
  }

  @PutMapping("/{id}")
  public AutorizacaoCompraResponse atualizar(
      @PathVariable("id") Long id, @Valid @RequestBody AutorizacaoCompraRequest request) {
    return service.atualizar(id, request);
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void remover(@PathVariable("id") Long id) {
    service.remover(id);
  }
}
