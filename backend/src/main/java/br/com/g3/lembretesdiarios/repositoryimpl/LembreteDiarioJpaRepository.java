package br.com.g3.lembretesdiarios.repositoryimpl;

import br.com.g3.lembretesdiarios.domain.LembreteDiario;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LembreteDiarioJpaRepository extends JpaRepository<LembreteDiario, Long> {
  List<LembreteDiario> findAllByDeletadoEmIsNullOrderByProximaExecucaoEmAsc();

  Optional<LembreteDiario> findByIdAndDeletadoEmIsNull(Long id);
}
