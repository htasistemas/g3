package br.com.g3.prontuario.controller;

import br.com.g3.prontuario.dto.ProntuarioAnexoRequest;
import br.com.g3.prontuario.dto.ProntuarioAnexoResponse;
import br.com.g3.prontuario.dto.ProntuarioRegistroListaResponse;
import br.com.g3.prontuario.dto.ProntuarioRegistroRequest;
import br.com.g3.prontuario.dto.ProntuarioRegistroResponse;
import br.com.g3.prontuario.dto.ProntuarioResumoResponse;
import br.com.g3.prontuario.service.ProntuarioService;
import jakarta.validation.Valid;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
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
@RequestMapping("/api")
public class ProntuarioController {
  private final ProntuarioService service;

  public ProntuarioController(ProntuarioService service) {
    this.service = service;
  }

  @GetMapping("/beneficiarios/{id}/prontuario/resumo")
  public ResponseEntity<ProntuarioResumoResponse> obterResumo(@PathVariable("id") Long id) {
    return ResponseEntity.ok(service.obterResumo(id));
  }

  @GetMapping("/beneficiarios/{id}/prontuario/registros")
  public ResponseEntity<ProntuarioRegistroListaResponse> listarRegistros(
      @PathVariable("id") Long id,
      @RequestParam(value = "tipo", required = false) String tipo,
      @RequestParam(value = "de", required = false) String dataInicio,
      @RequestParam(value = "ate", required = false) String dataFim,
      @RequestParam(value = "profissionalId", required = false) Long profissionalId,
      @RequestParam(value = "unidadeId", required = false) Long unidadeId,
      @RequestParam(value = "status", required = false) String status,
      @RequestParam(value = "texto", required = false) String texto,
      @RequestParam(value = "page", defaultValue = "0") int pagina,
      @RequestParam(value = "pageSize", defaultValue = "10") int tamanhoPagina) {
    return ResponseEntity.ok(
        service.listarRegistros(
            id,
            tipo,
            parseDate(dataInicio, true),
            parseDate(dataFim, false),
            profissionalId,
            unidadeId,
            status,
            texto,
            pagina,
            tamanhoPagina));
  }

  @PostMapping("/beneficiarios/{id}/prontuario/registros")
  public ResponseEntity<ProntuarioRegistroResponse> criarRegistro(
      @PathVariable("id") Long id, @Valid @RequestBody ProntuarioRegistroRequest request) {
    return ResponseEntity.ok(service.criarRegistro(id, request));
  }

  @PutMapping("/prontuario/registros/{registroId}")
  public ResponseEntity<ProntuarioRegistroResponse> atualizarRegistro(
      @PathVariable("registroId") Long registroId, @Valid @RequestBody ProntuarioRegistroRequest request) {
    return ResponseEntity.ok(service.atualizarRegistro(registroId, request));
  }

  @DeleteMapping("/prontuario/registros/{registroId}")
  public ResponseEntity<Void> removerRegistro(@PathVariable("registroId") Long registroId) {
    service.removerRegistro(registroId);
    return ResponseEntity.noContent().build();
  }

  @PostMapping("/prontuario/registros/{registroId}/anexos")
  public ResponseEntity<ProntuarioAnexoResponse> adicionarAnexo(
      @PathVariable("registroId") Long registroId, @Valid @RequestBody ProntuarioAnexoRequest request) {
    return ResponseEntity.ok(service.adicionarAnexo(registroId, request));
  }

  private LocalDateTime parseDate(String valor, boolean inicio) {
    if (valor == null || valor.trim().isEmpty()) {
      return null;
    }
    if (valor.length() == 10) {
      LocalDate data = LocalDate.parse(valor);
      return inicio ? data.atStartOfDay() : data.atTime(LocalTime.MAX);
    }
    return LocalDateTime.parse(valor);
  }
}
