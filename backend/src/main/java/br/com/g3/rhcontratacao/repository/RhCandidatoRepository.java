package br.com.g3.rhcontratacao.repository;

import br.com.g3.rhcontratacao.domain.RhCandidato;
import java.util.List;
import java.util.Optional;

public interface RhCandidatoRepository {
  RhCandidato salvar(RhCandidato candidato);
  Optional<RhCandidato> buscarPorId(Long id);
  List<RhCandidato> listar();
}
