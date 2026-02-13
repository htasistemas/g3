package br.com.g3.rhcontratacao.repository;

import br.com.g3.rhcontratacao.domain.RhPpd;
import java.util.Optional;

public interface RhPpdRepository {
  RhPpd salvar(RhPpd ppd);
  Optional<RhPpd> buscarPorProcesso(Long processoId);
}
