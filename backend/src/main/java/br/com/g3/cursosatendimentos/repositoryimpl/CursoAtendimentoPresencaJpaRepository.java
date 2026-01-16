package br.com.g3.cursosatendimentos.repositoryimpl;

import br.com.g3.cursosatendimentos.domain.CursoAtendimentoPresenca;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CursoAtendimentoPresencaJpaRepository
    extends JpaRepository<CursoAtendimentoPresenca, Long> {
  List<CursoAtendimentoPresenca> findByCursoAtendimentoIdAndDataAula(
      Long cursoId, LocalDate dataAula);

  Optional<CursoAtendimentoPresenca> findByCursoAtendimentoIdAndMatriculaIdAndDataAula(
      Long cursoId, Long matriculaId, LocalDate dataAula);
}
