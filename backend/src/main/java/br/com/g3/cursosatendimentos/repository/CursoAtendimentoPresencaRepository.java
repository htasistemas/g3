package br.com.g3.cursosatendimentos.repository;

import br.com.g3.cursosatendimentos.domain.CursoAtendimentoPresenca;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface CursoAtendimentoPresencaRepository {
  CursoAtendimentoPresenca salvar(CursoAtendimentoPresenca presenca);

  List<CursoAtendimentoPresenca> listarPorCursoEData(Long cursoId, LocalDate dataAula);

  Optional<CursoAtendimentoPresenca> buscarPorCursoMatriculaData(
      Long cursoId, Long matriculaId, LocalDate dataAula);
}
