package br.com.g3.chamadotecnico.repositoryimpl;

import br.com.g3.chamadotecnico.domain.ChamadoTecnicoAuditoriaVinculo;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChamadoTecnicoAuditoriaVinculoJpaRepository
    extends JpaRepository<ChamadoTecnicoAuditoriaVinculo, UUID> {
  List<ChamadoTecnicoAuditoriaVinculo> findByChamadoIdOrderByCriadoEmDesc(UUID chamadoId);
}
