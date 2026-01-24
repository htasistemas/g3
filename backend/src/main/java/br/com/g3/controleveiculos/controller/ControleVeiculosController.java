package br.com.g3.controleveiculos.controller;

import br.com.g3.controleveiculos.dto.DiarioBordoRequest;
import br.com.g3.controleveiculos.dto.DiarioBordoResponse;
import br.com.g3.controleveiculos.dto.VeiculoRequest;
import br.com.g3.controleveiculos.dto.VeiculoResponse;
import br.com.g3.controleveiculos.service.ControleVeiculosService;
import jakarta.validation.Valid;
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
@RequestMapping("/api/controle-veiculos")
public class ControleVeiculosController {
  private final ControleVeiculosService servico;

  public ControleVeiculosController(ControleVeiculosService servico) {
    this.servico = servico;
  }

  @GetMapping("/veiculos")
  public ResponseEntity<List<VeiculoResponse>> listarVeiculos() {
    return ResponseEntity.ok(servico.listarVeiculos());
  }

  @PostMapping("/veiculos")
  public ResponseEntity<VeiculoResponse> criarVeiculo(@Valid @RequestBody VeiculoRequest requisicao) {
    return ResponseEntity.ok(servico.criarVeiculo(requisicao));
  }

  @PutMapping("/veiculos/{id}")
  public ResponseEntity<VeiculoResponse> atualizarVeiculo(
      @PathVariable("id") Long id, @Valid @RequestBody VeiculoRequest requisicao) {
    return ResponseEntity.ok(servico.atualizarVeiculo(id, requisicao));
  }

  @DeleteMapping("/veiculos/{id}")
  public ResponseEntity<Void> excluirVeiculo(@PathVariable("id") Long id) {
    servico.excluirVeiculo(id);
    return ResponseEntity.noContent().build();
  }

  @GetMapping("/diario-bordo")
  public ResponseEntity<List<DiarioBordoResponse>> listarDiario() {
    return ResponseEntity.ok(servico.listarDiarios());
  }

  @PostMapping("/diario-bordo")
  public ResponseEntity<DiarioBordoResponse> criarDiario(
      @Valid @RequestBody DiarioBordoRequest requisicao) {
    return ResponseEntity.ok(servico.criarDiario(requisicao));
  }

  @PutMapping("/diario-bordo/{id}")
  public ResponseEntity<DiarioBordoResponse> atualizarDiario(
      @PathVariable("id") Long id, @Valid @RequestBody DiarioBordoRequest requisicao) {
    return ResponseEntity.ok(servico.atualizarDiario(id, requisicao));
  }

  @DeleteMapping("/diario-bordo/{id}")
  public ResponseEntity<Void> excluirDiario(@PathVariable("id") Long id) {
    servico.excluirDiario(id);
    return ResponseEntity.noContent().build();
  }
}
