package br.com.g3.doacaorealizada.controller;

import br.com.g3.doacaorealizada.dto.DoacaoRealizadaConsultaResponse;
import br.com.g3.doacaorealizada.dto.DoacaoRealizadaListaResponse;
import br.com.g3.doacaorealizada.dto.DoacaoRealizadaRequest;
import br.com.g3.doacaorealizada.dto.DoacaoRealizadaResponse;
import br.com.g3.doacaorealizada.service.DoacaoRealizadaService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/doacoes-realizadas")
public class DoacaoRealizadaController {
  private final DoacaoRealizadaService service;

  public DoacaoRealizadaController(DoacaoRealizadaService service) {
    this.service = service;
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public DoacaoRealizadaConsultaResponse criar(@Valid @RequestBody DoacaoRealizadaRequest request) {
    DoacaoRealizadaResponse response = service.criar(request);
    return new DoacaoRealizadaConsultaResponse(response);
  }

  @PutMapping("/{id}")
  public DoacaoRealizadaConsultaResponse atualizar(
      @PathVariable("id") Long id, @Valid @RequestBody DoacaoRealizadaRequest request) {
    DoacaoRealizadaResponse response = service.atualizar(id, request);
    return new DoacaoRealizadaConsultaResponse(response);
  }

  @GetMapping("/{id}")
  public DoacaoRealizadaConsultaResponse buscarPorId(@PathVariable("id") Long id) {
    return new DoacaoRealizadaConsultaResponse(service.buscarPorId(id));
  }

  @GetMapping
  public DoacaoRealizadaListaResponse listar() {
    List<DoacaoRealizadaResponse> doacoes = service.listar();
    return new DoacaoRealizadaListaResponse(doacoes);
  }
}
