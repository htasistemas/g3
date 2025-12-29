package br.com.g3.configuracoes.repositoryimpl;

import br.com.g3.configuracoes.domain.VersaoSistema;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VersaoSistemaJpaRepository extends JpaRepository<VersaoSistema, Long> {
  Optional<VersaoSistema> findTopByOrderByAtualizadoEmDesc();
}
