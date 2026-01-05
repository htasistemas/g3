package br.com.g3.oficios.repositoryimpl;

import br.com.g3.oficios.domain.OficioTramite;
import br.com.g3.oficios.repository.OficioTramiteRepository;
import java.util.List;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class OficioTramiteRepositoryImpl implements OficioTramiteRepository {
  private final OficioTramiteJpaRepository jpaRepository;

  public OficioTramiteRepositoryImpl(OficioTramiteJpaRepository jpaRepository) {
    this.jpaRepository = jpaRepository;
  }

  @Override
  public OficioTramite salvar(OficioTramite tramite) {
    return jpaRepository.save(tramite);
  }

  @Override
  public List<OficioTramite> listarPorOficio(Long oficioId) {
    return jpaRepository.findByOficioIdOrderByDataDescIdDesc(oficioId);
  }

  @Override
  @Transactional
  public void removerPorOficio(Long oficioId) {
    jpaRepository.deleteByOficioId(oficioId);
  }
}
