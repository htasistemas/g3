package br.com.g3.chamadotecnico.repository;

import br.com.g3.chamadotecnico.domain.ChamadoTecnico;
import java.util.Optional;
import java.util.UUID;

public interface ChamadoTecnicoRepository {
  ChamadoTecnico salvar(ChamadoTecnico chamado);

  Optional<ChamadoTecnico> buscarPorId(UUID id);

  void remover(ChamadoTecnico chamado);
}
