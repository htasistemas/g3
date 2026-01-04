package br.com.g3.planotrabalho.repositoryimpl;

import br.com.g3.planotrabalho.domain.PlanoTrabalho;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlanoTrabalhoJpaRepository extends JpaRepository<PlanoTrabalho, Long> {}
