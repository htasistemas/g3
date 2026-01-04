package br.com.g3.transparencia.repository;

import br.com.g3.transparencia.domain.TransparenciaTimeline;
import java.util.List;

public interface TransparenciaTimelineRepository {
  TransparenciaTimeline salvar(TransparenciaTimeline timeline);

  List<TransparenciaTimeline> listarPorTransparencia(Long transparenciaId);

  void removerPorTransparencia(Long transparenciaId);
}
