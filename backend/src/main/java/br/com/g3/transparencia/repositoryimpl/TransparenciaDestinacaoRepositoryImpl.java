package br.com.g3.transparencia.repositoryimpl;

import br.com.g3.transparencia.domain.TransparenciaDestinacao;
import br.com.g3.transparencia.repository.TransparenciaDestinacaoRepository;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class TransparenciaDestinacaoRepositoryImpl implements TransparenciaDestinacaoRepository {
  private final TransparenciaDestinacaoJpaRepository jpaRepository;

  public TransparenciaDestinacaoRepositoryImpl(
      TransparenciaDestinacaoJpaRepository jpaRepository) {
    this.jpaRepository = jpaRepository;
  }

  @Override
  public TransparenciaDestinacao salvar(TransparenciaDestinacao destinacao) {
    return jpaRepository.save(destinacao);
  }

  @Override
  public List<TransparenciaDestinacao> listarPorTransparencia(Long transparenciaId) {
    return jpaRepository.findAllByTransparenciaId(transparenciaId);
  }

  @Override
  public void removerPorTransparencia(Long transparenciaId) {
    jpaRepository.deleteByTransparenciaId(transparenciaId);
  }
}
