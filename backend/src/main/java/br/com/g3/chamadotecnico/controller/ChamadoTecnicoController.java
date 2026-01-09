package br.com.g3.chamadotecnico.controller;

import br.com.g3.chamadotecnico.dto.ChamadoTecnicoAcaoResponse;
import br.com.g3.chamadotecnico.dto.ChamadoTecnicoAnexoRequest;
import br.com.g3.chamadotecnico.dto.ChamadoTecnicoAnexoResponse;
import br.com.g3.chamadotecnico.dto.ChamadoTecnicoAtribuicaoRequest;
import br.com.g3.chamadotecnico.dto.ChamadoTecnicoAuditoriaVinculoRequest;
import br.com.g3.chamadotecnico.dto.ChamadoTecnicoAuditoriaVinculoResponse;
import br.com.g3.chamadotecnico.dto.ChamadoTecnicoComentarioRequest;
import br.com.g3.chamadotecnico.dto.ChamadoTecnicoComentarioResponse;
import br.com.g3.chamadotecnico.dto.ChamadoTecnicoCriacaoRequest;
import br.com.g3.chamadotecnico.dto.ChamadoTecnicoAtualizacaoRequest;
import br.com.g3.chamadotecnico.dto.ChamadoTecnicoListaResponse;
import br.com.g3.chamadotecnico.dto.ChamadoTecnicoResponse;
import br.com.g3.chamadotecnico.dto.ChamadoTecnicoStatusRequest;
import br.com.g3.chamadotecnico.service.ChamadoTecnicoService;
import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
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
@RequestMapping("/api/chamados")
public class ChamadoTecnicoController {
  private final ChamadoTecnicoService service;

  public ChamadoTecnicoController(ChamadoTecnicoService service) {
    this.service = service;
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public ChamadoTecnicoResponse criar(@Valid @RequestBody ChamadoTecnicoCriacaoRequest request) {
    return service.criar(request);
  }

  @GetMapping
  public ChamadoTecnicoListaResponse listar(
      @RequestParam(value = "status", required = false) String status,
      @RequestParam(value = "tipo", required = false) String tipo,
      @RequestParam(value = "prioridade", required = false) String prioridade,
      @RequestParam(value = "responsavel", required = false) String responsavel,
      @RequestParam(value = "modulo", required = false) String modulo,
      @RequestParam(value = "cliente", required = false) String cliente,
      @RequestParam(value = "data_inicio", required = false)
          @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
          LocalDate dataInicio,
      @RequestParam(value = "data_fim", required = false)
          @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
          LocalDate dataFim,
      @RequestParam(value = "texto", required = false) String texto,
      @RequestParam(value = "pagina", defaultValue = "1") int pagina,
      @RequestParam(value = "tamanho_pagina", defaultValue = "20") int tamanhoPagina) {
    return service.listar(
        status, tipo, prioridade, responsavel, modulo, cliente, dataInicio, dataFim, texto, pagina, tamanhoPagina);
  }

  @GetMapping("/{id}")
  public ChamadoTecnicoResponse detalhar(@PathVariable("id") UUID id) {
    return service.buscarPorId(id);
  }

  @PutMapping("/{id}")
  public ChamadoTecnicoResponse atualizar(
      @PathVariable("id") UUID id, @Valid @RequestBody ChamadoTecnicoAtualizacaoRequest request) {
    return service.atualizar(id, request);
  }

  @PostMapping("/{id}/status")
  public ChamadoTecnicoResponse alterarStatus(
      @PathVariable("id") UUID id, @Valid @RequestBody ChamadoTecnicoStatusRequest request) {
    return service.alterarStatus(id, request);
  }

  @PostMapping("/{id}/atribuir")
  public ChamadoTecnicoResponse atribuir(
      @PathVariable("id") UUID id, @Valid @RequestBody ChamadoTecnicoAtribuicaoRequest request) {
    return service.atribuirResponsavel(id, request);
  }

  @PostMapping("/{id}/comentarios")
  public ChamadoTecnicoComentarioResponse comentar(
      @PathVariable("id") UUID id, @Valid @RequestBody ChamadoTecnicoComentarioRequest request) {
    return service.adicionarComentario(id, request);
  }

  @GetMapping("/{id}/comentarios")
  public List<ChamadoTecnicoComentarioResponse> listarComentarios(@PathVariable("id") UUID id) {
    return service.listarComentarios(id);
  }

  @PostMapping("/{id}/anexos")
  public ChamadoTecnicoAnexoResponse adicionarAnexo(
      @PathVariable("id") UUID id, @Valid @RequestBody ChamadoTecnicoAnexoRequest request) {
    return service.adicionarAnexo(id, request);
  }

  @GetMapping("/{id}/anexos")
  public List<ChamadoTecnicoAnexoResponse> listarAnexos(@PathVariable("id") UUID id) {
    return service.listarAnexos(id);
  }

  @GetMapping("/{id}/acoes")
  public List<ChamadoTecnicoAcaoResponse> listarAcoes(@PathVariable("id") UUID id) {
    return service.listarAcoes(id);
  }

  @PostMapping("/{id}/vincular-auditoria")
  public ChamadoTecnicoAuditoriaVinculoResponse vincularAuditoria(
      @PathVariable("id") UUID id,
      @Valid @RequestBody ChamadoTecnicoAuditoriaVinculoRequest request) {
    return service.vincularAuditoria(id, request);
  }

  @GetMapping("/{id}/auditoria-vinculada")
  public List<ChamadoTecnicoAuditoriaVinculoResponse> listarAuditoria(@PathVariable("id") UUID id) {
    return service.listarAuditoriasVinculadas(id);
  }
}
