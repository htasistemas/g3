package br.com.g3.ocorrenciacrianca.controller;

import br.com.g3.ocorrenciacrianca.dto.OcorrenciaCriancaAnexoRequest;
import br.com.g3.ocorrenciacrianca.dto.OcorrenciaCriancaAnexoResponse;
import br.com.g3.ocorrenciacrianca.dto.OcorrenciaCriancaRequest;
import br.com.g3.ocorrenciacrianca.dto.OcorrenciaCriancaResponse;
import br.com.g3.ocorrenciacrianca.service.OcorrenciaCriancaService;
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
@RequestMapping("/api/ocorrencias-crianca")
public class OcorrenciaCriancaController {
  private final OcorrenciaCriancaService service;

  public OcorrenciaCriancaController(OcorrenciaCriancaService service) {
    this.service = service;
  }

  @GetMapping
  public ResponseEntity<List<OcorrenciaCriancaResponse>> listar() {
    return ResponseEntity.ok(service.listar());
  }

  @GetMapping("/{id}")
  public ResponseEntity<OcorrenciaCriancaResponse> buscar(@PathVariable("id") Long id) {
    return ResponseEntity.ok(service.buscar(id));
  }

  @PostMapping
  public ResponseEntity<OcorrenciaCriancaResponse> criar(
      @Valid @RequestBody OcorrenciaCriancaRequest request) {
    return ResponseEntity.ok(service.criar(request));
  }

  @PutMapping("/{id}")
  public ResponseEntity<OcorrenciaCriancaResponse> atualizar(
      @PathVariable("id") Long id, @Valid @RequestBody OcorrenciaCriancaRequest request) {
    return ResponseEntity.ok(service.atualizar(id, request));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> remover(@PathVariable("id") Long id) {
    service.remover(id);
    return ResponseEntity.noContent().build();
  }

  @GetMapping("/{id}/anexos")
  public ResponseEntity<List<OcorrenciaCriancaAnexoResponse>> listarAnexos(
      @PathVariable("id") Long id) {
    return ResponseEntity.ok(service.listarAnexos(id));
  }

  @PostMapping("/{id}/anexos")
  public ResponseEntity<OcorrenciaCriancaAnexoResponse> adicionarAnexo(
      @PathVariable("id") Long id,
      @Valid @RequestBody OcorrenciaCriancaAnexoRequest request) {
    return ResponseEntity.ok(service.adicionarAnexo(id, request));
  }

  @DeleteMapping("/{id}/anexos/{anexoId}")
  public ResponseEntity<Void> removerAnexo(
      @PathVariable("id") Long id, @PathVariable("anexoId") Long anexoId) {
    service.removerAnexo(id, anexoId);
    return ResponseEntity.noContent().build();
  }

  @GetMapping("/{id}/pdf/denuncia")
  public ResponseEntity<byte[]> imprimirDenuncia(@PathVariable("id") Long id) {
    byte[] conteudo = service.gerarPdfDenuncia(id);
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_PDF);
    headers.setContentDisposition(
        ContentDisposition.inline()
            .filename("ocorrencia-crianca-denuncia.pdf", StandardCharsets.UTF_8)
            .build());
    return new ResponseEntity<>(conteudo, headers, HttpStatus.OK);
  }

  @GetMapping("/{id}/pdf/conselho-tutelar")
  public ResponseEntity<byte[]> imprimirConselhoTutelar(@PathVariable("id") Long id) {
    byte[] conteudo = service.gerarPdfConselhoTutelar(id);
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_PDF);
    headers.setContentDisposition(
        ContentDisposition.inline()
            .filename("ocorrencia-crianca-conselho-tutelar.pdf", StandardCharsets.UTF_8)
            .build());
    return new ResponseEntity<>(conteudo, headers, HttpStatus.OK);
  }
}
