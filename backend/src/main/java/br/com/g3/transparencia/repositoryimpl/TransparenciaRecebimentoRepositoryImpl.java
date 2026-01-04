package br.com.g3.transparencia.repositoryimpl;

import br.com.g3.transparencia.domain.TransparenciaRecebimento;
import br.com.g3.transparencia.repository.TransparenciaRecebimentoRepository;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class TransparenciaRecebimentoRepositoryImpl implements TransparenciaRecebimentoRepository {
  private final TransparenciaRecebimentoJpaRepository jpaRepository;

  public TransparenciaRecebimentoRepositoryImpl(
      TransparenciaRecebimentoJpaRepository jpaRepository) {
    this.jpaRepository = jpaRepository;
  }

  @Override
  public TransparenciaRecebimento salvar(TransparenciaRecebimento recebimento) {
    return jpaRepository.save(recebimento);
  }

  @Override
  public List<TransparenciaRecebimento> listarPorTransparencia(Long transparenciaId) {
    return jpaRepository.findAllByTransparenciaId(transparenciaId);
  }

  @Override
  public void removerPorTransparencia(Long transparenciaId) {
    jpaRepository.deleteByTransparenciaId(transparenciaId);
  }
}
