package br.com.g3.planotrabalho.repositoryimpl;

import br.com.g3.planotrabalho.domain.PlanoTrabalhoAtividade;
import br.com.g3.planotrabalho.repository.PlanoTrabalhoAtividadeRepository;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class PlanoTrabalhoAtividadeRepositoryImpl implements PlanoTrabalhoAtividadeRepository {
  private final PlanoTrabalhoAtividadeJpaRepository jpaRepository;

  public PlanoTrabalhoAtividadeRepositoryImpl(PlanoTrabalhoAtividadeJpaRepository jpaRepository) {
    this.jpaRepository = jpaRepository;
  }

  @Override
  public PlanoTrabalhoAtividade salvar(PlanoTrabalhoAtividade atividade) {
    return jpaRepository.save(atividade);
  }

  @Override
  public List<PlanoTrabalhoAtividade> listarPorMeta(Long metaId) {
    return jpaRepository.findAllByMetaId(metaId);
  }

  @Override
  public void removerPorMeta(Long metaId) {
    jpaRepository.deleteByMetaId(metaId);
  }
}
