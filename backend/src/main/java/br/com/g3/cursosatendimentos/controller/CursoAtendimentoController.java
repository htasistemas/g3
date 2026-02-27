package br.com.g3.cursosatendimentos.controller;

import br.com.g3.cursosatendimentos.dto.CursoAtendimentoConsultaResponse;
import br.com.g3.cursosatendimentos.dto.CursoAtendimentoListaResponse;
import br.com.g3.cursosatendimentos.dto.CursoAtendimentoPresencaAnexoRequest;
import br.com.g3.cursosatendimentos.dto.CursoAtendimentoPresencaAnexoResponse;
import br.com.g3.cursosatendimentos.dto.CursoAtendimentoPresencaDataListaResponse;
import br.com.g3.cursosatendimentos.dto.CursoAtendimentoPresencaDataRequest;
import br.com.g3.cursosatendimentos.dto.CursoAtendimentoPresencaDataResponse;
import br.com.g3.cursosatendimentos.dto.CursoAtendimentoPresencaRequest;
import br.com.g3.cursosatendimentos.dto.CursoAtendimentoPresencaResponse;
import br.com.g3.cursosatendimentos.dto.CursoAtendimentoRequest;
import br.com.g3.cursosatendimentos.dto.CursoAtendimentoResponse;
import br.com.g3.cursosatendimentos.dto.CursoAtendimentoStatusRequest;
import br.com.g3.cursosatendimentos.service.CursoAtendimentoService;
import jakarta.validation.Valid;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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

  @GetMapping("/{id}/presencas/datas")
  public CursoAtendimentoPresencaDataListaResponse listarPresencaDatas(
      @PathVariable("id") Long id, @RequestParam(value = "pendentes", required = false) Boolean somentePendentes) {
    return service.listarPresencaDatas(id, somentePendentes);
  }

  @PostMapping("/{id}/presencas/datas")
  public CursoAtendimentoPresencaDataResponse criarPresencaData(
      @PathVariable("id") Long id, @RequestBody CursoAtendimentoPresencaDataRequest request) {
    return service.criarPresencaData(id, request);
  }

  @GetMapping("/{id}/presencas/datas/{dataId}")
  public CursoAtendimentoPresencaDataResponse obterPresencaData(
      @PathVariable("id") Long id, @PathVariable("dataId") Long dataId) {
    return service.obterPresencaData(id, dataId);
  }

  @PutMapping("/{id}/presencas/datas/{dataId}")
  public CursoAtendimentoPresencaDataResponse atualizarPresencaData(
      @PathVariable("id") Long id,
      @PathVariable("dataId") Long dataId,
      @RequestBody CursoAtendimentoPresencaDataRequest request) {
    return service.atualizarPresencaData(id, dataId, request);
  }

  @PatchMapping("/{id}/presencas/datas/{dataId}/cancelar")
  public CursoAtendimentoPresencaDataResponse cancelarPresencaData(
      @PathVariable("id") Long id, @PathVariable("dataId") Long dataId) {
    CursoAtendimentoPresencaDataRequest request = new CursoAtendimentoPresencaDataRequest();
    request.setStatus("CANCELADA");
    return service.atualizarPresencaData(id, dataId, request);
  }

  @DeleteMapping("/{id}/presencas/datas/{dataId}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void removerPresencaData(@PathVariable("id") Long id, @PathVariable("dataId") Long dataId) {
    service.removerPresencaData(id, dataId);
  }

  @GetMapping("/{id}/presencas/datas/{dataId}/itens")
  public CursoAtendimentoPresencaResponse listarPresencasPorData(
      @PathVariable("id") Long id, @PathVariable("dataId") Long dataId) {
    return service.listarPresencasPorDataId(id, dataId);
  }

  @PostMapping("/{id}/presencas/datas/{dataId}/itens")
  public CursoAtendimentoPresencaResponse salvarPresencasPorData(
      @PathVariable("id") Long id,
      @PathVariable("dataId") Long dataId,
      @RequestBody CursoAtendimentoPresencaRequest request) {
    return service.salvarPresencasPorDataId(id, dataId, request);
  }

  @GetMapping("/{id}/presencas/datas/{dataId}/anexos")
  public ResponseEntity<List<CursoAtendimentoPresencaAnexoResponse>> listarPresencaAnexos(
      @PathVariable("id") Long id, @PathVariable("dataId") Long dataId) {
    return ResponseEntity.ok(service.listarPresencaAnexos(id, dataId));
  }

  @PostMapping("/{id}/presencas/datas/{dataId}/anexos")
  public ResponseEntity<CursoAtendimentoPresencaAnexoResponse> adicionarPresencaAnexo(
      @PathVariable("id") Long id,
      @PathVariable("dataId") Long dataId,
      @Valid @RequestBody CursoAtendimentoPresencaAnexoRequest request) {
    return ResponseEntity.ok(service.adicionarPresencaAnexo(id, dataId, request));
  }

  @GetMapping("/{id}/presencas/datas/{dataId}/anexos/{anexoId}/arquivo")
  public ResponseEntity<Resource> baixarPresencaAnexo(
      @PathVariable("id") Long id,
      @PathVariable("dataId") Long dataId,
      @PathVariable("anexoId") Long anexoId) {
    Resource resource = service.obterPresencaAnexoArquivo(id, dataId, anexoId);
    return ResponseEntity.ok().contentType(obterMediaType(resource)).body(resource);
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void remover(@PathVariable("id") Long id) {
    service.remover(id);
  }

  private MediaType obterMediaType(Resource resource) {
    try {
      Path caminho = Paths.get(resource.getFile().getAbsolutePath());
      String contentType = Files.probeContentType(caminho);
      return contentType != null ? MediaType.parseMediaType(contentType) : MediaType.APPLICATION_OCTET_STREAM;
    } catch (Exception ex) {
      return MediaType.APPLICATION_OCTET_STREAM;
    }
  }
}
