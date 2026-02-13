package br.com.g3.rhcontratacao.repository;

import br.com.g3.rhcontratacao.domain.RhFichaAdmissao;
import java.util.Optional;

public interface RhFichaAdmissaoRepository {
  RhFichaAdmissao salvar(RhFichaAdmissao ficha);
  Optional<RhFichaAdmissao> buscarPorProcesso(Long processoId);
}
