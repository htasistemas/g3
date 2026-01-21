package br.com.g3.chamadas.fila.repository;

import br.com.g3.chamadas.fila.entity.FilaAtendimentoEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FilaAtendimentoRepository extends JpaRepository<FilaAtendimentoEntity, Long> {
  List<FilaAtendimentoEntity> findByStatusFilaOrderByPrioridadeDescDataHoraEntradaAsc(String statusFila);
}
