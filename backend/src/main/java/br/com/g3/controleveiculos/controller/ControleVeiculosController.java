package br.com.g3.controleveiculos.controller;

import br.com.g3.controleveiculos.dto.DiarioBordoRequest;
import br.com.g3.controleveiculos.dto.DiarioBordoResponse;
import br.com.g3.controleveiculos.dto.MotoristaAutorizadoRequest;
import br.com.g3.controleveiculos.dto.MotoristaAutorizadoResponse;
import br.com.g3.controleveiculos.dto.MotoristaDisponivelResponse;
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
import org.springframework.web.bind.annotation.RequestParam;
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

  @GetMapping("/motoristas-disponiveis")
  public ResponseEntity<List<MotoristaDisponivelResponse>> listarMotoristasDisponiveis(
      @RequestParam(value = "nome", required = false) String nome) {
    return ResponseEntity.ok(servico.listarMotoristasDisponiveis(nome));
  }

  @GetMapping("/motoristas-autorizados")
  public ResponseEntity<List<MotoristaAutorizadoResponse>> listarMotoristasAutorizados(
      @RequestParam(value = "veiculoId", required = false) Long veiculoId) {
    return ResponseEntity.ok(servico.listarMotoristasAutorizados(veiculoId));
  }

  @PostMapping("/motoristas-autorizados")
  public ResponseEntity<MotoristaAutorizadoResponse> criarMotoristaAutorizado(
      @Valid @RequestBody MotoristaAutorizadoRequest request) {
    return ResponseEntity.ok(servico.criarMotoristaAutorizado(request));
  }

  @PutMapping("/motoristas-autorizados/{id}")
  public ResponseEntity<MotoristaAutorizadoResponse> atualizarMotoristaAutorizado(
      @PathVariable("id") Long id, @Valid @RequestBody MotoristaAutorizadoRequest request) {
    return ResponseEntity.ok(servico.atualizarMotoristaAutorizado(id, request));
  }

  @DeleteMapping("/motoristas-autorizados/{id}")
  public ResponseEntity<Void> excluirMotoristaAutorizado(@PathVariable("id") Long id) {
    servico.excluirMotoristaAutorizado(id);
    return ResponseEntity.noContent().build();
  }
}
