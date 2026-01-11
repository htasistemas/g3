package br.com.g3.emprestimoseventos.repositoryimpl;

import br.com.g3.emprestimoseventos.domain.EventoEmprestimo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventoEmprestimoJpaRepository extends JpaRepository<EventoEmprestimo, Long> {}
