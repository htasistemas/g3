package br.com.g3.chamadotecnico.repositoryimpl;

import br.com.g3.chamadotecnico.domain.ChamadoTecnicoComentario;
import br.com.g3.chamadotecnico.repository.ChamadoTecnicoComentarioRepository;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Repository;

@Repository
public class ChamadoTecnicoComentarioRepositoryImpl
    implements ChamadoTecnicoComentarioRepository {
  private final ChamadoTecnicoComentarioJpaRepository jpaRepository;

  public ChamadoTecnicoComentarioRepositoryImpl(
      ChamadoTecnicoComentarioJpaRepository jpaRepository) {
    this.jpaRepository = jpaRepository;
  }

  @Override
  public ChamadoTecnicoComentario salvar(ChamadoTecnicoComentario comentario) {
    return jpaRepository.save(comentario);
  }

  @Override
  public List<ChamadoTecnicoComentario> listarPorChamado(UUID chamadoId) {
    return jpaRepository.findByChamadoIdOrderByCriadoEmDesc(chamadoId);
  }
}
