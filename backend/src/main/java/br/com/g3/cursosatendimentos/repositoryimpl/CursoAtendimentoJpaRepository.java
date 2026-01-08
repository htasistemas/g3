package br.com.g3.cursosatendimentos.repositoryimpl;

import br.com.g3.cursosatendimentos.domain.CursoAtendimento;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CursoAtendimentoJpaRepository extends JpaRepository<CursoAtendimento, Long> {}
