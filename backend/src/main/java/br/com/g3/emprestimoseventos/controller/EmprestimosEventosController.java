package br.com.g3.emprestimoseventos.controller;

import br.com.g3.emprestimoseventos.dto.AgendaDiaDetalheResponse;
import br.com.g3.emprestimoseventos.dto.AgendaResumoDiaResponse;
import br.com.g3.emprestimoseventos.dto.DisponibilidadeItemResponse;
import br.com.g3.emprestimoseventos.dto.EmprestimoEventoListaResponse;
import br.com.g3.emprestimoseventos.dto.EmprestimoEventoMovimentacaoListaResponse;
import br.com.g3.emprestimoseventos.dto.EmprestimoEventoRequest;
import br.com.g3.emprestimoseventos.dto.EmprestimoEventoResponse;
import br.com.g3.emprestimoseventos.dto.EventoEmprestimoRequest;
import br.com.g3.emprestimoseventos.dto.EventoEmprestimoResponse;
import br.com.g3.emprestimoseventos.service.EmprestimosEventosService;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
@RequestMapping("/api/emprestimos-eventos")
public class EmprestimosEventosController {
  private final EmprestimosEventosService servico;

  public EmprestimosEventosController(EmprestimosEventosService servico) {
    this.servico = servico;
  }

  @GetMapping
  public ResponseEntity<EmprestimoEventoListaResponse> listar(
      @RequestParam(value = "inicio", required = false) LocalDateTime inicio,
      @RequestParam(value = "fim", required = false) LocalDateTime fim,
      @RequestParam(value = "status", required = false) String status,
      @RequestParam(value = "evento", required = false) Long eventoId,
      @RequestParam(value = "item", required = false) Long itemId,
      @RequestParam(value = "unidade", required = false) Long unidadeId) {
    return ResponseEntity.ok(servico.listarEmprestimos(inicio, fim, status, eventoId, itemId, unidadeId));
  }

  @GetMapping("/{id}")
  public ResponseEntity<EmprestimoEventoResponse> obter(@PathVariable("id") Long id) {
    return ResponseEntity.ok(servico.obterEmprestimo(id));
  }

  @PostMapping
  public ResponseEntity<EmprestimoEventoResponse> criar(@RequestBody EmprestimoEventoRequest requisicao) {
    return ResponseEntity.ok(servico.criarEmprestimo(requisicao));
  }

  @PutMapping("/{id}")
  public ResponseEntity<EmprestimoEventoResponse> atualizar(
      @PathVariable("id") Long id, @RequestBody EmprestimoEventoRequest requisicao) {
    return ResponseEntity.ok(servico.atualizarEmprestimo(id, requisicao));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> excluir(@PathVariable("id") Long id) {
    servico.excluirEmprestimo(id);
    return ResponseEntity.noContent().build();
  }

  @PostMapping("/{id}/confirmar-retirada")
  public ResponseEntity<EmprestimoEventoResponse> confirmarRetirada(
      @PathVariable("id") Long id,
      @RequestParam(value = "usuarioId", required = false) Long usuarioId) {
    return ResponseEntity.ok(servico.confirmarRetirada(id, usuarioId));
  }

  @PostMapping("/{id}/confirmar-devolucao")
  public ResponseEntity<EmprestimoEventoResponse> confirmarDevolucao(
      @PathVariable("id") Long id,
      @RequestParam(value = "usuarioId", required = false) Long usuarioId) {
    return ResponseEntity.ok(servico.confirmarDevolucao(id, usuarioId));
  }

  @PostMapping("/{id}/cancelar")
  public ResponseEntity<EmprestimoEventoResponse> cancelar(
      @PathVariable("id") Long id,
      @RequestParam(value = "usuarioId", required = false) Long usuarioId) {
    return ResponseEntity.ok(servico.cancelarEmprestimo(id, usuarioId));
  }

  @GetMapping("/agenda/resumo")
  public ResponseEntity<List<AgendaResumoDiaResponse>> listarAgendaResumo(
      @RequestParam("inicio") LocalDate inicio, @RequestParam("fim") LocalDate fim) {
    return ResponseEntity.ok(servico.listarAgendaResumo(inicio, fim));
  }

  @GetMapping("/agenda/dia")
  public ResponseEntity<List<AgendaDiaDetalheResponse>> listarAgendaDia(
      @RequestParam("data") LocalDate data) {
    return ResponseEntity.ok(servico.listarAgendaDia(data));
  }

  @GetMapping("/disponibilidade")
  public ResponseEntity<DisponibilidadeItemResponse> consultarDisponibilidade(
      @RequestParam("itemId") Long itemId,
      @RequestParam("tipoItem") String tipoItem,
      @RequestParam(value = "quantidade", required = false) Integer quantidade,
      @RequestParam("inicio") LocalDateTime inicio,
      @RequestParam("fim") LocalDateTime fim,
      @RequestParam(value = "emprestimoId", required = false) Long emprestimoId) {
    return ResponseEntity.ok(
        servico.consultarDisponibilidade(itemId, tipoItem, quantidade, inicio, fim, emprestimoId));
  }

  @GetMapping("/{id}/movimentacoes")
  public ResponseEntity<EmprestimoEventoMovimentacaoListaResponse> listarMovimentacoes(
      @PathVariable("id") Long id) {
    return ResponseEntity.ok(servico.listarMovimentacoes(id));
  }

  @PostMapping("/eventos")
  public ResponseEntity<EventoEmprestimoResponse> criarEvento(
      @RequestBody EventoEmprestimoRequest requisicao) {
    return ResponseEntity.ok(servico.criarEvento(requisicao));
  }

  @PutMapping("/eventos/{id}")
  public ResponseEntity<EventoEmprestimoResponse> atualizarEvento(
      @PathVariable("id") Long id, @RequestBody EventoEmprestimoRequest requisicao) {
    return ResponseEntity.ok(servico.atualizarEvento(id, requisicao));
  }

  @DeleteMapping("/eventos/{id}")
  public ResponseEntity<Void> excluirEvento(@PathVariable("id") Long id) {
    servico.excluirEvento(id);
    return ResponseEntity.noContent().build();
  }

  @GetMapping("/eventos")
  public ResponseEntity<List<EventoEmprestimoResponse>> listarEventos() {       
    return ResponseEntity.ok(servico.listarEventos());
  }
}
