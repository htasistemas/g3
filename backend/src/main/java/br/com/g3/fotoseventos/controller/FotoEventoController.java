package br.com.g3.fotoseventos.controller;

import br.com.g3.fotoseventos.dto.FotoEventoDetalheResponse;
import br.com.g3.fotoseventos.dto.FotoEventoFotoAtualizacaoRequest;
import br.com.g3.fotoseventos.dto.FotoEventoFotoRequest;
import br.com.g3.fotoseventos.dto.FotoEventoFotoResponse;
import br.com.g3.fotoseventos.dto.FotoEventoListaResponse;
import br.com.g3.fotoseventos.dto.FotoEventoRequest;
import br.com.g3.fotoseventos.dto.FotoEventoResponse;
import br.com.g3.fotoseventos.service.FotoEventoService;
import jakarta.validation.Valid;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import org.springframework.core.io.Resource;
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
@RequestMapping("/api/fotos-eventos")
public class FotoEventoController {
  private final FotoEventoService service;

  public FotoEventoController(FotoEventoService service) {
    this.service = service;
  }

  @GetMapping
  public ResponseEntity<FotoEventoListaResponse> listar(
      @RequestParam(value = "busca", required = false) String busca,
      @RequestParam(value = "dataInicio", required = false) LocalDate dataInicio,
      @RequestParam(value = "dataFim", required = false) LocalDate dataFim,
      @RequestParam(value = "unidadeId", required = false) Long unidadeId,
      @RequestParam(value = "pagina", defaultValue = "0") int pagina,
      @RequestParam(value = "tamanho", defaultValue = "12") int tamanho) {
    return ResponseEntity.ok(service.listar(busca, dataInicio, dataFim, unidadeId, pagina, tamanho));
  }

  @GetMapping("/{id}")
  public ResponseEntity<FotoEventoDetalheResponse> obter(@PathVariable("id") Long id) {
    return ResponseEntity.ok(service.obter(id));
  }

  @GetMapping("/{id}/principal")
  public ResponseEntity<Resource> baixarFotoPrincipal(@PathVariable("id") Long id) {
    Resource resource = service.obterFotoPrincipal(id);
    return ResponseEntity.ok()
        .contentType(obterMediaType(resource))
        .body(resource);
  }

  @PostMapping
  public ResponseEntity<FotoEventoResponse> criar(@Valid @RequestBody FotoEventoRequest request) {
    return ResponseEntity.ok(service.criar(request));
  }

  @PutMapping("/{id}")
  public ResponseEntity<FotoEventoResponse> atualizar(
      @PathVariable("id") Long id, @Valid @RequestBody FotoEventoRequest request) {
    return ResponseEntity.ok(service.atualizar(id, request));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> excluir(@PathVariable("id") Long id) {
    service.excluir(id);
    return ResponseEntity.noContent().build();
  }

  @PostMapping("/{id}/fotos")
  public ResponseEntity<FotoEventoFotoResponse> adicionarFoto(
      @PathVariable("id") Long id, @Valid @RequestBody FotoEventoFotoRequest request) {
    return ResponseEntity.ok(service.adicionarFoto(id, request));
  }

  @PutMapping("/{id}/fotos/{fotoId}")
  public ResponseEntity<FotoEventoFotoResponse> atualizarFoto(
      @PathVariable("id") Long id,
      @PathVariable("fotoId") Long fotoId,
      @RequestBody FotoEventoFotoAtualizacaoRequest request) {
    return ResponseEntity.ok(service.atualizarFoto(id, fotoId, request));
  }

  @DeleteMapping("/{id}/fotos/{fotoId}")
  public ResponseEntity<Void> removerFoto(
      @PathVariable("id") Long id, @PathVariable("fotoId") Long fotoId) {
    service.removerFoto(id, fotoId);
    return ResponseEntity.noContent().build();
  }

  @GetMapping("/{id}/fotos/{fotoId}/arquivo")
  public ResponseEntity<Resource> baixarFoto(
      @PathVariable("id") Long id, @PathVariable("fotoId") Long fotoId) {
    Resource resource = service.obterArquivoFoto(id, fotoId);
    return ResponseEntity.ok()
        .contentType(obterMediaType(resource))
        .body(resource);
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
