package br.com.g3.tarefaspendencias.repositoryimpl;

import br.com.g3.tarefaspendencias.domain.TarefaPendencia;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TarefasPendenciasJpaRepository extends JpaRepository<TarefaPendencia, Long> {}
