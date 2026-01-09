package br.com.g3.chamadotecnico.repository;

import br.com.g3.chamadotecnico.domain.ChamadoTecnicoAnexo;
import java.util.List;
import java.util.UUID;

public interface ChamadoTecnicoAnexoRepository {
  ChamadoTecnicoAnexo salvar(ChamadoTecnicoAnexo anexo);

  List<ChamadoTecnicoAnexo> listarPorChamado(UUID chamadoId);
}
