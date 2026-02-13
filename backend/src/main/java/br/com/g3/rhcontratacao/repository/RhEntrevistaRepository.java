package br.com.g3.rhcontratacao.repository;

import br.com.g3.rhcontratacao.domain.RhEntrevista;
import java.util.List;
import java.util.Optional;

public interface RhEntrevistaRepository {
  RhEntrevista salvar(RhEntrevista entrevista);
  Optional<RhEntrevista> buscarPorId(Long id);
  List<RhEntrevista> listarPorProcesso(Long processoId);
}
