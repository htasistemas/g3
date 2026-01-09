package br.com.g3.chamadotecnico.repository;

import br.com.g3.chamadotecnico.domain.ChamadoTecnicoAuditoriaVinculo;
import java.util.List;
import java.util.UUID;

public interface ChamadoTecnicoAuditoriaVinculoRepository {
  ChamadoTecnicoAuditoriaVinculo salvar(ChamadoTecnicoAuditoriaVinculo vinculo);

  List<ChamadoTecnicoAuditoriaVinculo> listarPorChamado(UUID chamadoId);
}
