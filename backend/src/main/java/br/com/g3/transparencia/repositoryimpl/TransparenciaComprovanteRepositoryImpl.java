package br.com.g3.transparencia.repositoryimpl;

import br.com.g3.transparencia.domain.TransparenciaComprovante;
import br.com.g3.transparencia.repository.TransparenciaComprovanteRepository;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class TransparenciaComprovanteRepositoryImpl implements TransparenciaComprovanteRepository {
  private final TransparenciaComprovanteJpaRepository jpaRepository;

  public TransparenciaComprovanteRepositoryImpl(
      TransparenciaComprovanteJpaRepository jpaRepository) {
    this.jpaRepository = jpaRepository;
  }

  @Override
  public TransparenciaComprovante salvar(TransparenciaComprovante comprovante) {
    return jpaRepository.save(comprovante);
  }

  @Override
  public List<TransparenciaComprovante> listarPorTransparencia(Long transparenciaId) {
    return jpaRepository.findAllByTransparenciaId(transparenciaId);
  }

  @Override
  public void removerPorTransparencia(Long transparenciaId) {
    jpaRepository.deleteByTransparenciaId(transparenciaId);
  }
}
