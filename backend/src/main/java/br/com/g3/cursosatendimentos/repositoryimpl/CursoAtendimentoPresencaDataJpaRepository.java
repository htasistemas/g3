package br.com.g3.cursosatendimentos.repositoryimpl;

import br.com.g3.cursosatendimentos.domain.CursoAtendimentoPresencaData;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CursoAtendimentoPresencaDataJpaRepository
    extends JpaRepository<CursoAtendimentoPresencaData, Long> {
  Optional<CursoAtendimentoPresencaData> findByCursoAtendimentoIdAndDataAula(Long cursoId, LocalDate dataAula);

  List<CursoAtendimentoPresencaData> findAllByCursoAtendimentoIdOrderByDataAulaAscIdAsc(Long cursoId);
}
