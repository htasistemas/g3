package br.com.g3.patrimonio.repositoryimpl;

import br.com.g3.patrimonio.domain.PatrimonioItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PatrimonioJpaRepository extends JpaRepository<PatrimonioItem, Long> {}
