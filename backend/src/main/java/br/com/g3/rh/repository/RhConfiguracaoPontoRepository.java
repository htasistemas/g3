package br.com.g3.rh.repository;

import br.com.g3.rh.domain.RhConfiguracaoPonto;
import java.util.Optional;

public interface RhConfiguracaoPontoRepository {
  Optional<RhConfiguracaoPonto> buscarAtual();
  RhConfiguracaoPonto salvar(RhConfiguracaoPonto configuracao);
}
