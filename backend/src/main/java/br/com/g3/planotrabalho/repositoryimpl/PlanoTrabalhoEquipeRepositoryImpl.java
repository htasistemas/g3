package br.com.g3.planotrabalho.repositoryimpl;

import br.com.g3.planotrabalho.domain.PlanoTrabalhoEquipe;
import br.com.g3.planotrabalho.repository.PlanoTrabalhoEquipeRepository;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class PlanoTrabalhoEquipeRepositoryImpl implements PlanoTrabalhoEquipeRepository {
  private final PlanoTrabalhoEquipeJpaRepository jpaRepository;

  public PlanoTrabalhoEquipeRepositoryImpl(PlanoTrabalhoEquipeJpaRepository jpaRepository) {
    this.jpaRepository = jpaRepository;
  }

  @Override
  public PlanoTrabalhoEquipe salvar(PlanoTrabalhoEquipe equipe) {
    return jpaRepository.save(equipe);
  }

  @Override
  public List<PlanoTrabalhoEquipe> listarPorPlano(Long planoId) {
    return jpaRepository.findAllByPlanoTrabalhoId(planoId);
  }

  @Override
  public void removerPorPlano(Long planoId) {
    jpaRepository.deleteByPlanoTrabalhoId(planoId);
  }
}
