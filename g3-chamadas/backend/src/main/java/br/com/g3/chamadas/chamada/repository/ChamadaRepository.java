package br.com.g3.chamadas.chamada.repository;

import br.com.g3.chamadas.chamada.entity.ChamadaEntity;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChamadaRepository extends JpaRepository<ChamadaEntity, UUID> {
  List<ChamadaEntity> findTop10ByOrderByDataHoraChamadaDesc();
  Optional<ChamadaEntity> findTop1ByOrderByDataHoraChamadaDesc();
}
