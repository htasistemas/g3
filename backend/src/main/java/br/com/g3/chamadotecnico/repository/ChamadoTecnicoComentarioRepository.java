package br.com.g3.chamadotecnico.repository;

import br.com.g3.chamadotecnico.domain.ChamadoTecnicoComentario;
import java.util.List;
import java.util.UUID;

public interface ChamadoTecnicoComentarioRepository {
  ChamadoTecnicoComentario salvar(ChamadoTecnicoComentario comentario);

  List<ChamadoTecnicoComentario> listarPorChamado(UUID chamadoId);
}
