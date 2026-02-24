package br.com.g3.informacoesadministrativas.repositoryimpl;

import br.com.g3.informacoesadministrativas.domain.InformacaoAdministrativaAuditoria;
import br.com.g3.informacoesadministrativas.repository.InformacaoAdministrativaAuditoriaRepository;
import org.springframework.stereotype.Repository;

@Repository
public class InformacaoAdministrativaAuditoriaRepositoryImpl
    implements InformacaoAdministrativaAuditoriaRepository {
  private final InformacaoAdministrativaAuditoriaJpaRepository jpaRepository;

  public InformacaoAdministrativaAuditoriaRepositoryImpl(
      InformacaoAdministrativaAuditoriaJpaRepository jpaRepository) {
    this.jpaRepository = jpaRepository;
  }

  @Override
  public InformacaoAdministrativaAuditoria salvar(InformacaoAdministrativaAuditoria auditoria) {
    return jpaRepository.save(auditoria);
  }
}
