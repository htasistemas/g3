package br.com.g3.rhcontratacao.repository;

import br.com.g3.rhcontratacao.domain.RhProcessoContratacao;
import java.util.List;
import java.util.Optional;

public interface RhProcessoContratacaoRepository {
  RhProcessoContratacao salvar(RhProcessoContratacao processo);
  Optional<RhProcessoContratacao> buscarPorId(Long id);
  Optional<RhProcessoContratacao> buscarPorCandidatoId(Long candidatoId);
  List<RhProcessoContratacao> listar();
}
