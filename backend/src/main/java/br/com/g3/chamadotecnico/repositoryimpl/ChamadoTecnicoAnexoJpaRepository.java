package br.com.g3.chamadotecnico.repositoryimpl;

import br.com.g3.chamadotecnico.domain.ChamadoTecnicoAnexo;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChamadoTecnicoAnexoJpaRepository extends JpaRepository<ChamadoTecnicoAnexo, UUID> {
  List<ChamadoTecnicoAnexo> findByChamadoIdOrderByCriadoEmDesc(UUID chamadoId);
}
