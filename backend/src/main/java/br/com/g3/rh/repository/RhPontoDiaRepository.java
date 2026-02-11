package br.com.g3.rh.repository;

import br.com.g3.rh.domain.RhPontoDia;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface RhPontoDiaRepository {
  Optional<RhPontoDia> buscarPorId(Long id);
  Optional<RhPontoDia> buscarPorFuncionarioEData(Long funcionarioId, LocalDate data);
  List<RhPontoDia> listarPorFuncionarioEntreDatas(Long funcionarioId, LocalDate inicio, LocalDate fim);
  RhPontoDia salvar(RhPontoDia pontoDia);
}
