package br.com.g3.rhcontratacao.repository;

import br.com.g3.rhcontratacao.domain.RhCartaBanco;
import java.util.Optional;

public interface RhCartaBancoRepository {
  RhCartaBanco salvar(RhCartaBanco carta);
  Optional<RhCartaBanco> buscarPorProcesso(Long processoId);
}
