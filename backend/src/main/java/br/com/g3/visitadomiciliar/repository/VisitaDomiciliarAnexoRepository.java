package br.com.g3.visitadomiciliar.repository;

import br.com.g3.visitadomiciliar.domain.VisitaDomiciliarAnexo;
import java.util.List;

public interface VisitaDomiciliarAnexoRepository {
  VisitaDomiciliarAnexo salvar(VisitaDomiciliarAnexo anexo);

  void removerPorVisitaId(Long visitaId);

  List<VisitaDomiciliarAnexo> listarPorVisitaIds(List<Long> visitaIds);
}
