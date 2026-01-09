package br.com.g3.chamadotecnico.repositoryimpl;

import br.com.g3.chamadotecnico.domain.ChamadoTecnicoAuditoriaVinculo;
import br.com.g3.chamadotecnico.repository.ChamadoTecnicoAuditoriaVinculoRepository;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Repository;

@Repository
public class ChamadoTecnicoAuditoriaVinculoRepositoryImpl
    implements ChamadoTecnicoAuditoriaVinculoRepository {
  private final ChamadoTecnicoAuditoriaVinculoJpaRepository jpaRepository;

  public ChamadoTecnicoAuditoriaVinculoRepositoryImpl(
      ChamadoTecnicoAuditoriaVinculoJpaRepository jpaRepository) {
    this.jpaRepository = jpaRepository;
  }

  @Override
  public ChamadoTecnicoAuditoriaVinculo salvar(ChamadoTecnicoAuditoriaVinculo vinculo) {
    return jpaRepository.save(vinculo);
  }

  @Override
  public List<ChamadoTecnicoAuditoriaVinculo> listarPorChamado(UUID chamadoId) {
    return jpaRepository.findByChamadoIdOrderByCriadoEmDesc(chamadoId);
  }
}
