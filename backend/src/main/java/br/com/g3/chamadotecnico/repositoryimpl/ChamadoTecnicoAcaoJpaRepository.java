package br.com.g3.chamadotecnico.repositoryimpl;

import br.com.g3.chamadotecnico.domain.ChamadoTecnicoAcao;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChamadoTecnicoAcaoJpaRepository extends JpaRepository<ChamadoTecnicoAcao, UUID> {
  List<ChamadoTecnicoAcao> findByChamadoIdOrderByCriadoEmDesc(UUID chamadoId);
}
