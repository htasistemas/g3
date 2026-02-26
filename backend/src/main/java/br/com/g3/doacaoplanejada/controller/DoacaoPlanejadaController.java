package br.com.g3.doacaoplanejada.controller;

import br.com.g3.doacaoplanejada.dto.DoacaoPlanejadaConsultaResponse;
import br.com.g3.doacaoplanejada.dto.DoacaoPlanejadaListaResponse;
import br.com.g3.doacaoplanejada.dto.DoacaoPlanejadaRequest;
import br.com.g3.doacaoplanejada.dto.DoacaoPlanejadaResponse;
import br.com.g3.doacaoplanejada.service.DoacaoPlanejadaService;
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
@RequestMapping("/api/doacoes-planejadas")
public class DoacaoPlanejadaController {
  private final DoacaoPlanejadaService service;

  public DoacaoPlanejadaController(DoacaoPlanejadaService service) {
    this.service = service;
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public DoacaoPlanejadaConsultaResponse criar(@Valid @RequestBody DoacaoPlanejadaRequest request) {
    DoacaoPlanejadaResponse response = service.criar(request);
    return new DoacaoPlanejadaConsultaResponse(response);
  }

  @PutMapping("/{id}")
  public DoacaoPlanejadaConsultaResponse atualizar(
      @PathVariable("id") Long id, @Valid @RequestBody DoacaoPlanejadaRequest request) {
    DoacaoPlanejadaResponse response = service.atualizar(id, request);
    return new DoacaoPlanejadaConsultaResponse(response);
  }

  @GetMapping("/{id}")
  public DoacaoPlanejadaConsultaResponse buscarPorId(@PathVariable("id") Long id) {
    return new DoacaoPlanejadaConsultaResponse(service.buscarPorId(id));
  }

  @GetMapping
  public DoacaoPlanejadaListaResponse listar(
      @RequestParam(value = "beneficiarioId", required = false) Long beneficiarioId,
      @RequestParam(value = "vinculoFamiliarId", required = false) Long vinculoFamiliarId) {
    List<DoacaoPlanejadaResponse> doacoes;
    if (beneficiarioId != null) {
      doacoes = service.listarPorBeneficiario(beneficiarioId);
    } else if (vinculoFamiliarId != null) {
      doacoes = service.listarPorVinculoFamiliar(vinculoFamiliarId);
    } else {
      doacoes = service.listar();
    }
    return new DoacaoPlanejadaListaResponse(doacoes);
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void remover(@PathVariable("id") Long id) {
    service.remover(id);
  }
}
