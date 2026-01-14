package br.com.g3.gerenciamentodados.repositoryimpl;

import br.com.g3.gerenciamentodados.domain.GerenciamentoDadosConfiguracao;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GerenciamentoDadosConfiguracaoJpaRepository
    extends JpaRepository<GerenciamentoDadosConfiguracao, Long> {
  Optional<GerenciamentoDadosConfiguracao> findTopByOrderByAtualizadoEmDesc();
}
