package br.com.g3.cursosatendimentos.repository;

import br.com.g3.cursosatendimentos.domain.CursoAtendimentoPresencaData;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface CursoAtendimentoPresencaDataRepository {
  CursoAtendimentoPresencaData salvar(CursoAtendimentoPresencaData presencaData);

  Optional<CursoAtendimentoPresencaData> buscarPorId(Long id);

  Optional<CursoAtendimentoPresencaData> buscarPorCursoEData(Long cursoId, LocalDate dataAula);

  List<CursoAtendimentoPresencaData> listarPorCurso(Long cursoId);

  void remover(CursoAtendimentoPresencaData presencaData);
}
