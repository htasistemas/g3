package br.com.g3.rh.repositoryimpl;

import br.com.g3.rh.domain.RhPontoDia;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RhPontoDiaJpaRepository extends JpaRepository<RhPontoDia, Long> {
  @EntityGraph(attributePaths = {"marcacoes"})
  Optional<RhPontoDia> findByFuncionarioIdAndData(Long funcionarioId, LocalDate data);

  @EntityGraph(attributePaths = {"marcacoes"})
  List<RhPontoDia> findAllByFuncionarioIdAndDataBetweenOrderByDataAsc(
      Long funcionarioId, LocalDate inicio, LocalDate fim);
}
