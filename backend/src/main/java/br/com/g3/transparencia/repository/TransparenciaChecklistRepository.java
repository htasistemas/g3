package br.com.g3.transparencia.repository;

import br.com.g3.transparencia.domain.TransparenciaChecklist;
import java.util.List;

public interface TransparenciaChecklistRepository {
  TransparenciaChecklist salvar(TransparenciaChecklist checklist);

  List<TransparenciaChecklist> listarPorTransparencia(Long transparenciaId);

  void removerPorTransparencia(Long transparenciaId);
}
