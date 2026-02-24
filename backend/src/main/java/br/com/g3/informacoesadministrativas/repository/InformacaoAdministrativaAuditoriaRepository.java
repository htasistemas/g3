package br.com.g3.informacoesadministrativas.repository;

import br.com.g3.informacoesadministrativas.domain.InformacaoAdministrativaAuditoria;

public interface InformacaoAdministrativaAuditoriaRepository {
  InformacaoAdministrativaAuditoria salvar(InformacaoAdministrativaAuditoria auditoria);
}
