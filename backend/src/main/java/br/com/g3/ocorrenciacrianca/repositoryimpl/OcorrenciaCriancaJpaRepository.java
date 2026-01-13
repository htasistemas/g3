package br.com.g3.ocorrenciacrianca.repositoryimpl;

import br.com.g3.ocorrenciacrianca.domain.OcorrenciaCrianca;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OcorrenciaCriancaJpaRepository extends JpaRepository<OcorrenciaCrianca, Long> {}
