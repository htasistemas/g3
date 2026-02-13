package br.com.g3.rhcontratacao.repositoryimpl;

import br.com.g3.rhcontratacao.domain.RhAuditoriaContratacao;
import br.com.g3.rhcontratacao.repository.RhAuditoriaContratacaoRepository;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class RhAuditoriaContratacaoRepositoryImpl implements RhAuditoriaContratacaoRepository {
  private final RhAuditoriaContratacaoJpaRepository jpaRepository;

  public RhAuditoriaContratacaoRepositoryImpl(RhAuditoriaContratacaoJpaRepository jpaRepository) {
    this.jpaRepository = jpaRepository;
  }

  @Override
  public RhAuditoriaContratacao salvar(RhAuditoriaContratacao auditoria) {
    return jpaRepository.save(auditoria);
  }

  @Override
  public List<RhAuditoriaContratacao> listarPorProcesso(Long processoId) {
    return jpaRepository.findByProcessoIdOrderByCriadoEmDesc(processoId);
  }
}
