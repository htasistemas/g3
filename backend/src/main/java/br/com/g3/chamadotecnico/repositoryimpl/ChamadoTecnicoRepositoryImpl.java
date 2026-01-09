package br.com.g3.chamadotecnico.repositoryimpl;

import br.com.g3.chamadotecnico.domain.ChamadoTecnico;
import br.com.g3.chamadotecnico.repository.ChamadoTecnicoRepository;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Repository;

@Repository
public class ChamadoTecnicoRepositoryImpl implements ChamadoTecnicoRepository {
  private final ChamadoTecnicoJpaRepository jpaRepository;

  public ChamadoTecnicoRepositoryImpl(ChamadoTecnicoJpaRepository jpaRepository) {
    this.jpaRepository = jpaRepository;
  }

  @Override
  public ChamadoTecnico salvar(ChamadoTecnico chamado) {
    return jpaRepository.save(chamado);
  }

  @Override
  public Optional<ChamadoTecnico> buscarPorId(UUID id) {
    return jpaRepository.findById(id);
  }

  @Override
  public void remover(ChamadoTecnico chamado) {
    jpaRepository.delete(chamado);
  }
}
