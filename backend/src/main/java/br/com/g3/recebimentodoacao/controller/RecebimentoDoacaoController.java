package br.com.g3.recebimentodoacao.controller;

import br.com.g3.recebimentodoacao.dto.DoadorRequest;
import br.com.g3.recebimentodoacao.dto.DoadorResponse;
import br.com.g3.recebimentodoacao.dto.RecebimentoDoacaoListaResponse;
import br.com.g3.recebimentodoacao.dto.RecebimentoDoacaoRequest;
import br.com.g3.recebimentodoacao.dto.RecebimentoDoacaoResponse;
import br.com.g3.recebimentodoacao.service.RecebimentoDoacaoService;
import java.util.List;
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
@RequestMapping("/api/recebimentos-doacao")
public class RecebimentoDoacaoController {
  private final RecebimentoDoacaoService service;

  public RecebimentoDoacaoController(RecebimentoDoacaoService service) {
    this.service = service;
  }

  @PostMapping
  public ResponseEntity<RecebimentoDoacaoResponse> criar(@RequestBody RecebimentoDoacaoRequest request) {
    return ResponseEntity.ok(service.criarRecebimento(request));
  }

  @PutMapping("/{id}")
  public ResponseEntity<RecebimentoDoacaoResponse> atualizar(
      @PathVariable("id") Long id, @RequestBody RecebimentoDoacaoRequest request) {
    return ResponseEntity.ok(service.atualizarRecebimento(id, request));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> excluir(@PathVariable("id") Long id) {
    service.excluirRecebimento(id);
    return ResponseEntity.noContent().build();
  }

  @GetMapping
  public ResponseEntity<RecebimentoDoacaoListaResponse> listar() {
    return ResponseEntity.ok(service.listarRecebimentos());
  }

  @PostMapping("/doadores")
  public ResponseEntity<DoadorResponse> criarDoador(@RequestBody DoadorRequest request) {
    return ResponseEntity.ok(service.criarDoador(request));
  }

  @GetMapping("/doadores")
  public ResponseEntity<List<DoadorResponse>> listarDoadores() {
    return ResponseEntity.ok(service.listarDoadores());
  }

  @DeleteMapping("/doadores/{id}")
  public ResponseEntity<Void> excluirDoador(@PathVariable("id") Long id) {
    service.excluirDoador(id);
    return ResponseEntity.noContent().build();
  }
}
