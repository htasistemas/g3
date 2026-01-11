package br.com.g3.emprestimoseventos.repositoryimpl;

import br.com.g3.emprestimoseventos.domain.EmprestimoEvento;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmprestimoEventoJpaRepository extends JpaRepository<EmprestimoEvento, Long> {}
