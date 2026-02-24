package br.com.g3.informacoesadministrativas.repositoryimpl;

import br.com.g3.informacoesadministrativas.domain.InformacaoAdministrativa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InformacaoAdministrativaJpaRepository extends JpaRepository<InformacaoAdministrativa, Long> {}
