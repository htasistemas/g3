package br.com.g3.rhcontratacao.repository;

import br.com.g3.rhcontratacao.domain.RhTermo;
import java.util.List;
import java.util.Optional;

public interface RhTermoRepository {
  RhTermo salvar(RhTermo termo);
  Optional<RhTermo> buscarPorId(Long id);
  Optional<RhTermo> buscarPorProcessoETipo(Long processoId, String tipo);
  List<RhTermo> listarPorProcesso(Long processoId);
}
