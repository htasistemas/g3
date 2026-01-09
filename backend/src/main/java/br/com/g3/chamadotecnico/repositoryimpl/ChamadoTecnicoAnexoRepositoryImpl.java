package br.com.g3.chamadotecnico.repositoryimpl;

import br.com.g3.chamadotecnico.domain.ChamadoTecnicoAnexo;
import br.com.g3.chamadotecnico.repository.ChamadoTecnicoAnexoRepository;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Repository;

@Repository
public class ChamadoTecnicoAnexoRepositoryImpl implements ChamadoTecnicoAnexoRepository {
  private final ChamadoTecnicoAnexoJpaRepository jpaRepository;

  public ChamadoTecnicoAnexoRepositoryImpl(ChamadoTecnicoAnexoJpaRepository jpaRepository) {
    this.jpaRepository = jpaRepository;
  }

  @Override
  public ChamadoTecnicoAnexo salvar(ChamadoTecnicoAnexo anexo) {
    return jpaRepository.save(anexo);
  }

  @Override
  public List<ChamadoTecnicoAnexo> listarPorChamado(UUID chamadoId) {
    return jpaRepository.findByChamadoIdOrderByCriadoEmDesc(chamadoId);
  }
}
