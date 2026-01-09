package br.com.g3.chamadotecnico.repository;

import br.com.g3.chamadotecnico.domain.ChamadoTecnicoAcao;
import java.util.List;
import java.util.UUID;

public interface ChamadoTecnicoAcaoRepository {
  ChamadoTecnicoAcao salvar(ChamadoTecnicoAcao acao);

  List<ChamadoTecnicoAcao> listarPorChamado(UUID chamadoId);
}
