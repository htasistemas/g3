package br.com.g3.rhcontratacao.controller;

import br.com.g3.rhcontratacao.dto.RhAtualizarStatusRequest;
import br.com.g3.rhcontratacao.dto.RhArquivoRequest;
import br.com.g3.rhcontratacao.dto.RhArquivoResponse;
import br.com.g3.rhcontratacao.dto.RhAuditoriaContratacaoResponse;
import br.com.g3.rhcontratacao.dto.RhCartaBancoRequest;
import br.com.g3.rhcontratacao.dto.RhCartaBancoResponse;
import br.com.g3.rhcontratacao.dto.RhCandidatoRequest;
import br.com.g3.rhcontratacao.dto.RhCandidatoResponse;
import br.com.g3.rhcontratacao.dto.RhCandidatoResumoResponse;
import br.com.g3.rhcontratacao.dto.RhDocumentoItemRequest;
import br.com.g3.rhcontratacao.dto.RhDocumentoItemResponse;
import br.com.g3.rhcontratacao.dto.RhEntrevistaRequest;
import br.com.g3.rhcontratacao.dto.RhEntrevistaResponse;
import br.com.g3.rhcontratacao.dto.RhFichaAdmissaoRequest;
import br.com.g3.rhcontratacao.dto.RhFichaAdmissaoResponse;
import br.com.g3.rhcontratacao.dto.RhPpdRequest;
import br.com.g3.rhcontratacao.dto.RhPpdResponse;
import br.com.g3.rhcontratacao.dto.RhProcessoContratacaoResponse;
import br.com.g3.rhcontratacao.dto.RhTermoRequest;
import br.com.g3.rhcontratacao.dto.RhTermoResponse;
import br.com.g3.rhcontratacao.service.RhContratacaoService;
import java.util.List;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/rh/contratacao")
public class RhContratacaoController {
  private final RhContratacaoService service;

  public RhContratacaoController(RhContratacaoService service) {
    this.service = service;
  }

  @GetMapping("/candidatos")
  public List<RhCandidatoResumoResponse> listarCandidatos(@RequestParam(value = "termo", required = false) String termo) {
    return service.listarCandidatos(termo);
  }

  @GetMapping("/candidatos/{id}")
  public RhCandidatoResponse buscarCandidato(@PathVariable Long id) {
    return service.buscarCandidato(id);
  }

  @PostMapping("/candidatos")
  public RhCandidatoResponse criarCandidato(
      @RequestParam("usuarioId") Long usuarioId,
      @RequestBody RhCandidatoRequest request) {
    return service.criarCandidato(request, usuarioId);
  }

  @PutMapping("/candidatos/{id}")
  public RhCandidatoResponse atualizarCandidato(
      @PathVariable Long id,
      @RequestParam("usuarioId") Long usuarioId,
      @RequestBody RhCandidatoRequest request) {
    return service.atualizarCandidato(id, request, usuarioId);
  }

  @DeleteMapping("/candidatos/{id}")
  public void inativarCandidato(@PathVariable Long id, @RequestParam("usuarioId") Long usuarioId) {
    service.inativarCandidato(id, usuarioId);
  }

  @GetMapping("/processos/por-candidato/{candidatoId}")
  public RhProcessoContratacaoResponse buscarProcesso(@PathVariable Long candidatoId) {
    return service.buscarProcessoPorCandidato(candidatoId);
  }

  @PutMapping("/processos/{processoId}/status")
  public RhProcessoContratacaoResponse atualizarStatus(
      @PathVariable Long processoId,
      @RequestParam("usuarioId") Long usuarioId,
      @RequestBody RhAtualizarStatusRequest request) {
    return service.atualizarStatus(processoId, request, usuarioId);
  }

  @GetMapping("/processos/{processoId}/entrevistas")
  public List<RhEntrevistaResponse> listarEntrevistas(@PathVariable Long processoId) {
    return service.listarEntrevistas(processoId);
  }

  @PostMapping("/processos/{processoId}/entrevistas")
  public RhEntrevistaResponse salvarEntrevista(
      @PathVariable Long processoId,
      @RequestParam("usuarioId") Long usuarioId,
      @RequestBody RhEntrevistaRequest request) {
    return service.salvarEntrevista(processoId, request, usuarioId);
  }

  @GetMapping("/processos/{processoId}/ficha")
  public RhFichaAdmissaoResponse buscarFicha(@PathVariable Long processoId) {
    return service.buscarFichaAdmissao(processoId);
  }

  @PutMapping("/processos/{processoId}/ficha")
  public RhFichaAdmissaoResponse salvarFicha(
      @PathVariable Long processoId,
      @RequestParam("usuarioId") Long usuarioId,
      @RequestBody RhFichaAdmissaoRequest request) {
    return service.salvarFichaAdmissao(processoId, request, usuarioId);
  }

  @GetMapping("/processos/{processoId}/documentos")
  public List<RhDocumentoItemResponse> listarDocumentos(@PathVariable Long processoId) {
    return service.listarDocumentos(processoId);
  }

  @PutMapping("/documentos/{id}")
  public RhDocumentoItemResponse atualizarDocumento(
      @PathVariable Long id,
      @RequestParam("usuarioId") Long usuarioId,
      @RequestBody RhDocumentoItemRequest request) {
    return service.atualizarDocumentoItem(id, request, usuarioId);
  }

  @GetMapping("/processos/{processoId}/arquivos")
  public List<RhArquivoResponse> listarArquivos(@PathVariable Long processoId) {
    return service.listarArquivos(processoId);
  }

  @PostMapping("/processos/{processoId}/arquivos")
  public RhArquivoResponse adicionarArquivo(
      @PathVariable Long processoId,
      @RequestParam("usuarioId") Long usuarioId,
      @RequestBody RhArquivoRequest request) {
    return service.adicionarArquivo(processoId, request, usuarioId);
  }

  @GetMapping("/arquivos/{arquivoId}/download")
  public ResponseEntity<Resource> downloadArquivo(@PathVariable Long arquivoId) {
    Resource resource = service.obterArquivo(arquivoId);
    return ResponseEntity.ok()
        .contentType(MediaType.APPLICATION_OCTET_STREAM)
        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=arquivo")
        .body(resource);
  }

  @GetMapping("/processos/{processoId}/termos")
  public List<RhTermoResponse> listarTermos(@PathVariable Long processoId) {
    return service.listarTermos(processoId);
  }

  @PostMapping("/processos/{processoId}/termos")
  public RhTermoResponse salvarTermo(
      @PathVariable Long processoId,
      @RequestParam("usuarioId") Long usuarioId,
      @RequestBody RhTermoRequest request) {
    return service.salvarTermo(processoId, request, usuarioId);
  }

  @GetMapping("/processos/{processoId}/ppd")
  public RhPpdResponse buscarPpd(@PathVariable Long processoId) {
    return service.buscarPpd(processoId);
  }

  @PutMapping("/processos/{processoId}/ppd")
  public RhPpdResponse salvarPpd(
      @PathVariable Long processoId,
      @RequestParam("usuarioId") Long usuarioId,
      @RequestBody RhPpdRequest request) {
    return service.salvarPpd(processoId, request, usuarioId);
  }

  @GetMapping("/processos/{processoId}/carta-banco")
  public RhCartaBancoResponse buscarCartaBanco(@PathVariable Long processoId) {
    return service.buscarCartaBanco(processoId);
  }

  @PutMapping("/processos/{processoId}/carta-banco")
  public RhCartaBancoResponse salvarCartaBanco(
      @PathVariable Long processoId,
      @RequestParam("usuarioId") Long usuarioId,
      @RequestBody RhCartaBancoRequest request) {
    return service.salvarCartaBanco(processoId, request, usuarioId);
  }

  @GetMapping("/processos/{processoId}/auditoria")
  public List<RhAuditoriaContratacaoResponse> listarAuditoria(@PathVariable Long processoId) {
    return service.listarAuditoria(processoId);
  }
}
