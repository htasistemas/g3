package br.com.g3.chamadotecnico.repositoryimpl;

import br.com.g3.chamadotecnico.domain.ChamadoTecnicoAcao;
import br.com.g3.chamadotecnico.repository.ChamadoTecnicoAcaoRepository;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Repository;

@Repository
public class ChamadoTecnicoAcaoRepositoryImpl implements ChamadoTecnicoAcaoRepository {
  private final ChamadoTecnicoAcaoJpaRepository jpaRepository;

  public ChamadoTecnicoAcaoRepositoryImpl(ChamadoTecnicoAcaoJpaRepository jpaRepository) {
    this.jpaRepository = jpaRepository;
  }

  @Override
  public ChamadoTecnicoAcao salvar(ChamadoTecnicoAcao acao) {
    return jpaRepository.save(acao);
  }

  @Override
  public List<ChamadoTecnicoAcao> listarPorChamado(UUID chamadoId) {
    return jpaRepository.findByChamadoIdOrderByCriadoEmDesc(chamadoId);
  }
}
