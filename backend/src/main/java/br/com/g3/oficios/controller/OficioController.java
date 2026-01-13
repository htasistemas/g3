package br.com.g3.oficios.controller;

import br.com.g3.oficios.dto.OficioImagemRequest;
import br.com.g3.oficios.dto.OficioImagemResponse;
import br.com.g3.oficios.dto.OficioListaResponse;
import br.com.g3.oficios.dto.OficioPdfAssinadoRequest;
import br.com.g3.oficios.dto.OficioRequest;
import br.com.g3.oficios.dto.OficioResponse;
import br.com.g3.oficios.service.OficioService;
import jakarta.validation.Valid;
import java.nio.charset.StandardCharsets;
import java.util.List;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/oficios")
public class OficioController {
  private final OficioService service;

  public OficioController(OficioService service) {
    this.service = service;
  }

  @GetMapping
  public ResponseEntity<OficioListaResponse> listar() {
    return ResponseEntity.ok(service.listar());
  }

  @GetMapping("/{id}")
  public ResponseEntity<OficioResponse> obter(@PathVariable("id") Long id) {
    return ResponseEntity.ok(service.obter(id));
  }

  @PostMapping
  public ResponseEntity<OficioResponse> criar(@Valid @RequestBody OficioRequest request) {
    return ResponseEntity.ok(service.criar(request));
  }

  @PutMapping("/{id}")
  public ResponseEntity<OficioResponse> atualizar(
      @PathVariable("id") Long id, @Valid @RequestBody OficioRequest request) {
    return ResponseEntity.ok(service.atualizar(id, request));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> excluir(@PathVariable("id") Long id) {
    service.excluir(id);
    return ResponseEntity.noContent().build();
  }

  @PostMapping("/{id}/pdf-assinado")
  public ResponseEntity<OficioResponse> salvarPdfAssinado(
      @PathVariable("id") Long id, @RequestBody OficioPdfAssinadoRequest request) {
    return ResponseEntity.ok(service.salvarPdfAssinado(id, request));
  }

  @GetMapping("/{id}/pdf-assinado")
  public ResponseEntity<byte[]> obterPdfAssinado(@PathVariable("id") Long id) {
    byte[] conteudo = service.obterPdfAssinado(id);
    String tipo = service.obterPdfAssinadoTipo(id);
    String nome = service.obterPdfAssinadoNome(id);
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.parseMediaType(tipo));
    headers.setContentDisposition(ContentDisposition.inline().filename(nome, StandardCharsets.UTF_8).build());
    return new ResponseEntity<>(conteudo, headers, HttpStatus.OK);
  }

  @DeleteMapping("/{id}/pdf-assinado")
  public ResponseEntity<Void> removerPdfAssinado(@PathVariable("id") Long id) {
    service.removerPdfAssinado(id);
    return ResponseEntity.noContent().build();
  }

  @GetMapping("/{id}/imagens")
  public ResponseEntity<List<OficioImagemResponse>> listarImagens(@PathVariable("id") Long id) {
    return ResponseEntity.ok(service.listarImagens(id));
  }

  @PostMapping("/{id}/imagens")
  public ResponseEntity<OficioImagemResponse> adicionarImagem(
      @PathVariable("id") Long id, @Valid @RequestBody OficioImagemRequest request) {
    return ResponseEntity.ok(service.adicionarImagem(id, request));
  }

  @DeleteMapping("/{id}/imagens/{imagemId}")
  public ResponseEntity<Void> removerImagem(
      @PathVariable("id") Long id, @PathVariable("imagemId") Long imagemId) {
    service.removerImagem(id, imagemId);
    return ResponseEntity.noContent().build();
  }
}
