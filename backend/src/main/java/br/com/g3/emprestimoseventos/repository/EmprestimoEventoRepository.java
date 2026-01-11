package br.com.g3.emprestimoseventos.repository;

import br.com.g3.emprestimoseventos.domain.EmprestimoEvento;
import br.com.g3.emprestimoseventos.repositoryimpl.EmprestimoEventoAgendaRegistro;
import br.com.g3.emprestimoseventos.repositoryimpl.EmprestimoEventoAgendaRow;
import br.com.g3.emprestimoseventos.repositoryimpl.EmprestimoEventoDisponibilidadeResumo;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface EmprestimoEventoRepository {
  EmprestimoEvento salvar(EmprestimoEvento emprestimo);

  Optional<EmprestimoEvento> buscarPorId(Long id);

  List<EmprestimoEvento> listarPorFiltros(
      LocalDateTime inicio,
      LocalDateTime fim,
      String status,
      Long eventoId,
      Long itemId,
      Long unidadeId);

  List<EmprestimoEventoAgendaRegistro> listarAgendaRegistros(
      LocalDateTime inicio, LocalDateTime fim);

  List<EmprestimoEventoAgendaRow> listarAgendaDia(LocalDateTime inicioDia, LocalDateTime fimDia);

  List<EmprestimoEventoDisponibilidadeResumo> buscarConflitosItem(
      Long itemId,
      String tipoItem,
      LocalDateTime inicio,
      LocalDateTime fim,
      Long emprestimoIgnorarId);

  Integer obterEstoqueAtual(Long itemId);
}
