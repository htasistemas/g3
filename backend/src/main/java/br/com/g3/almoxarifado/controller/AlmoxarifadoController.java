package br.com.g3.almoxarifado.controller;

import br.com.g3.almoxarifado.dto.AlmoxarifadoItemCriacaoRequest;
import br.com.g3.almoxarifado.dto.AlmoxarifadoItemListaResponse;
import br.com.g3.almoxarifado.dto.AlmoxarifadoItemResponse;
import br.com.g3.almoxarifado.dto.AlmoxarifadoMovimentacaoCadastroResponse;
import br.com.g3.almoxarifado.dto.AlmoxarifadoMovimentacaoListaResponse;
import br.com.g3.almoxarifado.dto.AlmoxarifadoMovimentacaoRequest;
import br.com.g3.almoxarifado.dto.AlmoxarifadoMovimentacaoResponse;
import br.com.g3.almoxarifado.dto.AlmoxarifadoProximoCodigoResponse;
import br.com.g3.almoxarifado.service.AlmoxarifadoService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/almoxarifado")
public class AlmoxarifadoController {
  private final AlmoxarifadoService service;

  public AlmoxarifadoController(AlmoxarifadoService service) {
    this.service = service;
  }

  @GetMapping("/items")
  public AlmoxarifadoItemListaResponse listarItens() {
    List<AlmoxarifadoItemResponse> itens = service.listarItens();
    return new AlmoxarifadoItemListaResponse(itens);
  }

  @GetMapping("/items/next-code")
  public AlmoxarifadoProximoCodigoResponse obterProximoCodigo() {
    return new AlmoxarifadoProximoCodigoResponse(service.obterProximoCodigo());
  }

  @PostMapping("/items")
  public AlmoxarifadoItemResponse criarItem(@Valid @RequestBody AlmoxarifadoItemCriacaoRequest request) {
    return service.criarItem(request);
  }

  @PutMapping("/items/{id}")
  public AlmoxarifadoItemResponse atualizarItem(
      @PathVariable("id") Long id, @Valid @RequestBody AlmoxarifadoItemCriacaoRequest request) {
    return service.atualizarItem(id, request);
  }

  @GetMapping("/movements")
  public AlmoxarifadoMovimentacaoListaResponse listarMovimentacoes() {
    List<AlmoxarifadoMovimentacaoResponse> movimentos = service.listarMovimentacoes();
    return new AlmoxarifadoMovimentacaoListaResponse(movimentos);
  }

  @PostMapping("/movements")
  public AlmoxarifadoMovimentacaoCadastroResponse registrarMovimentacao(
      @Valid @RequestBody AlmoxarifadoMovimentacaoRequest request) {
    return service.registrarMovimentacao(request);
  }
}
