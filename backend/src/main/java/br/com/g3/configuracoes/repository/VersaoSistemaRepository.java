package br.com.g3.configuracoes.repository;

import br.com.g3.configuracoes.domain.VersaoSistema;
import java.util.Optional;

public interface VersaoSistemaRepository {
  Optional<VersaoSistema> buscarAtual();

  VersaoSistema salvar(VersaoSistema versaoSistema);
}
