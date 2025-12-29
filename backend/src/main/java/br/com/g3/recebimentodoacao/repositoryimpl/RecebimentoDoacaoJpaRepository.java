package br.com.g3.recebimentodoacao.repositoryimpl;

import br.com.g3.recebimentodoacao.domain.RecebimentoDoacao;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecebimentoDoacaoJpaRepository extends JpaRepository<RecebimentoDoacao, Long> {}
