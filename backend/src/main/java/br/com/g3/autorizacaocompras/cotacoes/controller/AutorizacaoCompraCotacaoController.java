package br.com.g3.autorizacaocompras.cotacoes.controller;

import br.com.g3.autorizacaocompras.cotacoes.dto.AutorizacaoCompraCotacaoRequest;
import br.com.g3.autorizacaocompras.cotacoes.dto.AutorizacaoCompraCotacaoResponse;
import br.com.g3.autorizacaocompras.cotacoes.service.AutorizacaoCompraCotacaoService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/financeiro/autorizacao-compras/{id}/cotacoes")
public class AutorizacaoCompraCotacaoController {
  private final AutorizacaoCompraCotacaoService service;

  public AutorizacaoCompraCotacaoController(AutorizacaoCompraCotacaoService service) {
    this.service = service;
  }

  @GetMapping
  public List<AutorizacaoCompraCotacaoResponse> listar(@PathVariable("id") Long compraId) {
    return service.listarPorCompraId(compraId);
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public AutorizacaoCompraCotacaoResponse criar(
      @PathVariable("id") Long compraId,
      @Valid @RequestBody AutorizacaoCompraCotacaoRequest request) {
    return service.criar(compraId, request);
  }

  @DeleteMapping("/{cotacaoId}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void remover(
      @PathVariable("id") Long compraId, @PathVariable("cotacaoId") Long cotacaoId) {
    service.remover(compraId, cotacaoId);
  }
}
