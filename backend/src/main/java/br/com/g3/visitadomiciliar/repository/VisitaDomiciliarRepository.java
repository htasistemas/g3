package br.com.g3.visitadomiciliar.repository;

import br.com.g3.visitadomiciliar.domain.VisitaDomiciliar;
import java.util.List;
import java.util.Optional;

public interface VisitaDomiciliarRepository {
  VisitaDomiciliar salvar(VisitaDomiciliar visita);

  Optional<VisitaDomiciliar> buscarPorId(Long id);

  List<VisitaDomiciliar> listar();

  void remover(VisitaDomiciliar visita);
}
