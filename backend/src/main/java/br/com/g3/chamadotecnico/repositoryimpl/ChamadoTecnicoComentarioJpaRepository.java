package br.com.g3.chamadotecnico.repositoryimpl;

import br.com.g3.chamadotecnico.domain.ChamadoTecnicoComentario;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChamadoTecnicoComentarioJpaRepository
    extends JpaRepository<ChamadoTecnicoComentario, UUID> {
  List<ChamadoTecnicoComentario> findByChamadoIdOrderByCriadoEmDesc(UUID chamadoId);
}
