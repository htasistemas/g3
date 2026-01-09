package br.com.g3.chamadotecnico.repositoryimpl;

import br.com.g3.chamadotecnico.domain.ChamadoTecnico;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChamadoTecnicoJpaRepository extends JpaRepository<ChamadoTecnico, UUID> {}
