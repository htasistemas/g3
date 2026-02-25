package br.com.g3.manualsistema.repositoryimpl;

import br.com.g3.manualsistema.domain.ManualSistemaSecao;
import br.com.g3.manualsistema.repository.ManualSistemaSecaoRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class ManualSistemaSecaoRepositoryImpl implements ManualSistemaSecaoRepository {
  private final ManualSistemaSecaoJpaRepository jpaRepository;

  public ManualSistemaSecaoRepositoryImpl(ManualSistemaSecaoJpaRepository jpaRepository) {
    this.jpaRepository = jpaRepository;
  }

  @Override
  public List<ManualSistemaSecao> listarOrdenado() {
    return jpaRepository.findAllByOrderByOrdemAsc();
  }

  @Override
  public Optional<ManualSistemaSecao> buscarPorSlug(String slug) {
    return jpaRepository.findBySlug(slug);
  }

  @Override
  public ManualSistemaSecao salvar(ManualSistemaSecao secao) {
    return jpaRepository.save(secao);
  }
}
