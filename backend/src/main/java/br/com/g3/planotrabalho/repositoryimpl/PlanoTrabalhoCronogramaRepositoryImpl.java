package br.com.g3.planotrabalho.repositoryimpl;

import br.com.g3.planotrabalho.domain.PlanoTrabalhoCronograma;
import br.com.g3.planotrabalho.repository.PlanoTrabalhoCronogramaRepository;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class PlanoTrabalhoCronogramaRepositoryImpl implements PlanoTrabalhoCronogramaRepository {
  private final PlanoTrabalhoCronogramaJpaRepository jpaRepository;

  public PlanoTrabalhoCronogramaRepositoryImpl(
      PlanoTrabalhoCronogramaJpaRepository jpaRepository) {
    this.jpaRepository = jpaRepository;
  }

  @Override
  public PlanoTrabalhoCronograma salvar(PlanoTrabalhoCronograma cronograma) {
    return jpaRepository.save(cronograma);
  }

  @Override
  public List<PlanoTrabalhoCronograma> listarPorPlano(Long planoId) {
    return jpaRepository.findAllByPlanoTrabalhoId(planoId);
  }

  @Override
  public void removerPorPlano(Long planoId) {
    jpaRepository.deleteByPlanoTrabalhoId(planoId);
  }
}
