package br.com.g3.informacoesadministrativas.repositoryimpl;

import br.com.g3.informacoesadministrativas.domain.InformacaoAdministrativaAuditoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InformacaoAdministrativaAuditoriaJpaRepository
    extends JpaRepository<InformacaoAdministrativaAuditoria, Long> {}
