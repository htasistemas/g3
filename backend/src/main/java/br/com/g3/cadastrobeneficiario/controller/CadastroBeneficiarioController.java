package br.com.g3.cadastrobeneficiario.controller;

import br.com.g3.cadastrobeneficiario.dto.CadastroBeneficiarioConsultaResponse;
import br.com.g3.cadastrobeneficiario.dto.CadastroBeneficiarioCriacaoRequest;
import br.com.g3.cadastrobeneficiario.dto.CadastroBeneficiarioListaResponse;
import br.com.g3.cadastrobeneficiario.dto.CadastroBeneficiarioResponse;
import br.com.g3.cadastrobeneficiario.service.CadastroBeneficiarioService;
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
@RequestMapping("/api/beneficiarios")
public class CadastroBeneficiarioController {
  private final CadastroBeneficiarioService service;

  public CadastroBeneficiarioController(CadastroBeneficiarioService service) {
    this.service = service;
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public CadastroBeneficiarioConsultaResponse criar(@Valid @RequestBody CadastroBeneficiarioCriacaoRequest request) {
    CadastroBeneficiarioResponse response = service.criar(request);
    return new CadastroBeneficiarioConsultaResponse(response);
  }

  @PutMapping("/{id}")
  public CadastroBeneficiarioConsultaResponse atualizar(
      @PathVariable("id") Long id, @Valid @RequestBody CadastroBeneficiarioCriacaoRequest request) {
    CadastroBeneficiarioResponse response = service.atualizar(id, request);
    return new CadastroBeneficiarioConsultaResponse(response);
  }

  @GetMapping("/{id}")
  public CadastroBeneficiarioConsultaResponse buscarPorId(@PathVariable("id") Long id) {
    return new CadastroBeneficiarioConsultaResponse(service.buscarPorId(id));
  }

  @GetMapping
  public CadastroBeneficiarioListaResponse listar(@RequestParam(value = "nome", required = false) String nome) {
    List<CadastroBeneficiarioResponse> beneficiarios = service.listar(nome);
    return new CadastroBeneficiarioListaResponse(beneficiarios);
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void remover(@PathVariable("id") Long id) {
    service.remover(id);
  }
}
