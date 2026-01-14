package br.com.g3.gerenciamentodados.repository;

import br.com.g3.gerenciamentodados.domain.GerenciamentoDadosConfiguracao;
import java.util.Optional;

public interface GerenciamentoDadosConfiguracaoRepository {
  Optional<GerenciamentoDadosConfiguracao> buscarAtual();

  GerenciamentoDadosConfiguracao salvar(GerenciamentoDadosConfiguracao configuracao);
}
