package br.com.g3.unidadeassistencial.repositoryimpl;

import br.com.g3.unidadeassistencial.domain.UnidadeAssistencial;
import java.util.Optional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UnidadeAssistencialJpaRepository extends JpaRepository<UnidadeAssistencial, Long> {
  Optional<UnidadeAssistencial> findTopByOrderByAtualizadoEmDesc();

  Optional<UnidadeAssistencial> findTopByUnidadePrincipalTrue();

  @Modifying
  @Query("update UnidadeAssistencial unidade set unidade.unidadePrincipal = false where unidade.unidadePrincipal = true")
  void limparUnidadePrincipal();
}
