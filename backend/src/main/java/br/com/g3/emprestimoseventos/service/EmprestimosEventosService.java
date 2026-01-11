package br.com.g3.emprestimoseventos.service;

import br.com.g3.emprestimoseventos.dto.AgendaDiaDetalheResponse;
import br.com.g3.emprestimoseventos.dto.AgendaResumoDiaResponse;
import br.com.g3.emprestimoseventos.dto.DisponibilidadeItemResponse;
import br.com.g3.emprestimoseventos.dto.EmprestimoEventoListaResponse;
import br.com.g3.emprestimoseventos.dto.EmprestimoEventoMovimentacaoListaResponse;
import br.com.g3.emprestimoseventos.dto.EmprestimoEventoRequest;
import br.com.g3.emprestimoseventos.dto.EmprestimoEventoResponse;
import br.com.g3.emprestimoseventos.dto.EventoEmprestimoRequest;
import br.com.g3.emprestimoseventos.dto.EventoEmprestimoResponse;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface EmprestimosEventosService {
  EmprestimoEventoListaResponse listarEmprestimos(
      LocalDateTime inicio,
      LocalDateTime fim,
      String status,
      Long eventoId,
      Long itemId,
      Long unidadeId);

  EmprestimoEventoResponse obterEmprestimo(Long id);

  EmprestimoEventoResponse criarEmprestimo(EmprestimoEventoRequest requisicao);

  EmprestimoEventoResponse atualizarEmprestimo(Long id, EmprestimoEventoRequest requisicao);

  void excluirEmprestimo(Long id);

  EmprestimoEventoResponse confirmarRetirada(Long id, Long usuarioId);

  EmprestimoEventoResponse confirmarDevolucao(Long id, Long usuarioId);

  EmprestimoEventoResponse cancelarEmprestimo(Long id, Long usuarioId);

  List<AgendaResumoDiaResponse> listarAgendaResumo(LocalDate inicio, LocalDate fim);

  List<AgendaDiaDetalheResponse> listarAgendaDia(LocalDate data);

  DisponibilidadeItemResponse consultarDisponibilidade(
      Long itemId,
      String tipoItem,
      Integer quantidade,
      LocalDateTime inicio,
      LocalDateTime fim,
      Long emprestimoId);

  EmprestimoEventoMovimentacaoListaResponse listarMovimentacoes(Long emprestimoId);

  EventoEmprestimoResponse criarEvento(EventoEmprestimoRequest requisicao);

  EventoEmprestimoResponse atualizarEvento(Long id, EventoEmprestimoRequest requisicao);

  void excluirEvento(Long id);

  List<EventoEmprestimoResponse> listarEventos();
}
