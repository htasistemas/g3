package br.com.g3.lembretesdiarios.repositoryimpl;

import br.com.g3.lembretesdiarios.domain.LembreteDiario;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface LembreteDiarioJpaRepository extends JpaRepository<LembreteDiario, Long> {
  List<LembreteDiario> findAllByDeletadoEmIsNullOrderByProximaExecucaoEmAsc();
  List<LembreteDiario> findAllByDeletadoEmIsNullAndUsuarioIdOrderByProximaExecucaoEmAsc(Long usuarioId);

  @Query(
      """
      select l from LembreteDiario l
      where l.deletadoEm is null
        and (l.todosUsuarios = true or l.usuarioId = :usuarioId)
      order by l.proximaExecucaoEm asc
      """)
  List<LembreteDiario> listarPorUsuarioOuTodos(@Param("usuarioId") Long usuarioId);

  Optional<LembreteDiario> findByIdAndDeletadoEmIsNull(Long id);
}
