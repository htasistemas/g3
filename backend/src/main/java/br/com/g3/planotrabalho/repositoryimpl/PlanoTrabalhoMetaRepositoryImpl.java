package br.com.g3.planotrabalho.repositoryimpl;

import br.com.g3.planotrabalho.domain.PlanoTrabalhoMeta;
import br.com.g3.planotrabalho.repository.PlanoTrabalhoMetaRepository;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class PlanoTrabalhoMetaRepositoryImpl implements PlanoTrabalhoMetaRepository {
  private final PlanoTrabalhoMetaJpaRepository jpaRepository;

  public PlanoTrabalhoMetaRepositoryImpl(PlanoTrabalhoMetaJpaRepository jpaRepository) {
    this.jpaRepository = jpaRepository;
  }

  @Override
  public PlanoTrabalhoMeta salvar(PlanoTrabalhoMeta meta) {
    return jpaRepository.save(meta);
  }

  @Override
  public List<PlanoTrabalhoMeta> listarPorPlano(Long planoId) {
    return jpaRepository.findAllByPlanoTrabalhoId(planoId);
  }

  @Override
  public void removerPorPlano(Long planoId) {
    jpaRepository.deleteByPlanoTrabalhoId(planoId);
  }
}
