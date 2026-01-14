package br.com.g3.gerenciamentodados.repositoryimpl;

import br.com.g3.gerenciamentodados.domain.GerenciamentoDadosConfiguracao;
import br.com.g3.gerenciamentodados.repository.GerenciamentoDadosConfiguracaoRepository;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class GerenciamentoDadosConfiguracaoRepositoryImpl
    implements GerenciamentoDadosConfiguracaoRepository {
  private final GerenciamentoDadosConfiguracaoJpaRepository jpaRepository;

  public GerenciamentoDadosConfiguracaoRepositoryImpl(
      GerenciamentoDadosConfiguracaoJpaRepository jpaRepository) {
    this.jpaRepository = jpaRepository;
  }

  @Override
  public Optional<GerenciamentoDadosConfiguracao> buscarAtual() {
    return jpaRepository.findTopByOrderByAtualizadoEmDesc();
  }

  @Override
  public GerenciamentoDadosConfiguracao salvar(GerenciamentoDadosConfiguracao configuracao) {
    return jpaRepository.save(configuracao);
  }
}
