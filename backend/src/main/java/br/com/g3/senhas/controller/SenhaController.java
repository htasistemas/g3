package br.com.g3.senhas.controller;

import br.com.g3.senhas.dto.SenhaChamadaResponse;
import br.com.g3.senhas.dto.SenhaChamarRequest;
import br.com.g3.senhas.dto.SenhaEmitirRequest;
import br.com.g3.senhas.dto.SenhaFilaResponse;
import br.com.g3.senhas.dto.SenhaFinalizarRequest;
import br.com.g3.senhas.service.SenhaService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/senhas")
public class SenhaController {
  private final SenhaService senhaService;

  public SenhaController(SenhaService senhaService) {
    this.senhaService = senhaService;
  }

  @PostMapping("/emitir")
  public ResponseEntity<SenhaFilaResponse> emitir(@Valid @RequestBody SenhaEmitirRequest request) {
    return ResponseEntity.ok(senhaService.emitirSenha(request));
  }

  @GetMapping("/aguardando")
  public ResponseEntity<List<SenhaFilaResponse>> listarAguardando(
      @RequestParam(value = "unidadeId", required = false) Long unidadeId) {
    return ResponseEntity.ok(senhaService.listarAguardando(unidadeId));
  }

  @PostMapping("/chamar")
  public ResponseEntity<SenhaChamadaResponse> chamar(
      @Valid @RequestBody SenhaChamarRequest request,
      @RequestHeader(value = "X-Usuario", required = false) String usuario) {
    String chamadoPor = usuario != null && !usuario.trim().isEmpty() ? usuario.trim() : "Sistema";
    return ResponseEntity.ok(senhaService.chamarSenha(request, chamadoPor));
  }

  @PostMapping("/finalizar")
  public ResponseEntity<Void> finalizar(@Valid @RequestBody SenhaFinalizarRequest request) {
    senhaService.finalizarSenha(request.getChamadaId());
    return ResponseEntity.noContent().build();
  }

  @PostMapping("/finalizar-fila")
  public ResponseEntity<Void> finalizarFila(@RequestParam("filaId") Long filaId) {
    senhaService.finalizarPorFila(filaId);
    return ResponseEntity.noContent().build();
  }

  @GetMapping("/painel")
  public ResponseEntity<List<SenhaChamadaResponse>> painel(
      @RequestParam(value = "unidadeId", required = false) Long unidadeId,
      @RequestParam(value = "limite", required = false) Integer limite) {
    return ResponseEntity.ok(senhaService.listarPainel(unidadeId, limite));
  }

  @GetMapping("/historico")
  public ResponseEntity<List<SenhaChamadaResponse>> historico(
      @RequestParam(value = "unidadeId", required = false) Long unidadeId,
      @RequestParam(value = "limite", required = false) Integer limite) {
    return ResponseEntity.ok(senhaService.listarHistorico(unidadeId, limite));
  }

  @GetMapping("/atual")
  public ResponseEntity<SenhaChamadaResponse> atual(
      @RequestParam(value = "unidadeId", required = false) Long unidadeId) {
    return ResponseEntity.ok(senhaService.obterAtual(unidadeId));
  }
}
