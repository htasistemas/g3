package br.com.g3.planotrabalho.repositoryimpl;

import br.com.g3.planotrabalho.domain.PlanoTrabalhoEtapa;
import br.com.g3.planotrabalho.repository.PlanoTrabalhoEtapaRepository;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class PlanoTrabalhoEtapaRepositoryImpl implements PlanoTrabalhoEtapaRepository {
  private final PlanoTrabalhoEtapaJpaRepository jpaRepository;

  public PlanoTrabalhoEtapaRepositoryImpl(PlanoTrabalhoEtapaJpaRepository jpaRepository) {
    this.jpaRepository = jpaRepository;
  }

  @Override
  public PlanoTrabalhoEtapa salvar(PlanoTrabalhoEtapa etapa) {
    return jpaRepository.save(etapa);
  }

  @Override
  public List<PlanoTrabalhoEtapa> listarPorAtividade(Long atividadeId) {
    return jpaRepository.findAllByAtividadeId(atividadeId);
  }

  @Override
  public void removerPorAtividade(Long atividadeId) {
    jpaRepository.deleteByAtividadeId(atividadeId);
  }
}
