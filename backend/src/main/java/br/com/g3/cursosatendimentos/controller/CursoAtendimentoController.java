package br.com.g3.cursosatendimentos.controller;

import br.com.g3.cursosatendimentos.dto.CursoAtendimentoConsultaResponse;
import br.com.g3.cursosatendimentos.dto.CursoAtendimentoListaResponse;
import br.com.g3.cursosatendimentos.dto.CursoAtendimentoPresencaRequest;
import br.com.g3.cursosatendimentos.dto.CursoAtendimentoPresencaResponse;
import br.com.g3.cursosatendimentos.dto.CursoAtendimentoRequest;
import br.com.g3.cursosatendimentos.dto.CursoAtendimentoResponse;
import br.com.g3.cursosatendimentos.dto.CursoAtendimentoStatusRequest;
import br.com.g3.cursosatendimentos.service.CursoAtendimentoService;
import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/cursos-atendimentos")
public class CursoAtendimentoController {
  private final CursoAtendimentoService service;

  public CursoAtendimentoController(CursoAtendimentoService service) {
    this.service = service;
  }

  @GetMapping
  public CursoAtendimentoListaResponse listar() {
    List<CursoAtendimentoResponse> records = service.listar();
    return new CursoAtendimentoListaResponse(records);
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public CursoAtendimentoConsultaResponse criar(@Valid @RequestBody CursoAtendimentoRequest request) {
    return new CursoAtendimentoConsultaResponse(service.criar(request));
  }

  @PutMapping("/{id}")
  public CursoAtendimentoConsultaResponse atualizar(
      @PathVariable("id") Long id, @Valid @RequestBody CursoAtendimentoRequest request) {
    return new CursoAtendimentoConsultaResponse(service.atualizar(id, request));
  }

  @PatchMapping("/{id}/status")
  public CursoAtendimentoConsultaResponse atualizarStatus(
      @PathVariable("id") Long id, @RequestBody CursoAtendimentoStatusRequest request) {
    return new CursoAtendimentoConsultaResponse(service.atualizarStatus(id, request));
  }

  @GetMapping("/{id}/presencas")
  public CursoAtendimentoPresencaResponse listarPresencas(
      @PathVariable("id") Long id, @RequestParam("data") LocalDate dataAula) {
    return service.listarPresencas(id, dataAula);
  }

  @PostMapping("/{id}/presencas")
  public CursoAtendimentoPresencaResponse salvarPresencas(
      @PathVariable("id") Long id, @RequestBody CursoAtendimentoPresencaRequest request) {
    return service.salvarPresencas(id, request);
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void remover(@PathVariable("id") Long id) {
    service.remover(id);
  }
}
