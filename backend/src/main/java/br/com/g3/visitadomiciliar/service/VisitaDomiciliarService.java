package br.com.g3.visitadomiciliar.service;

import br.com.g3.visitadomiciliar.dto.VisitaDomiciliarListaResponse;
import br.com.g3.visitadomiciliar.dto.VisitaDomiciliarRequest;
import br.com.g3.visitadomiciliar.dto.VisitaDomiciliarResponse;

public interface VisitaDomiciliarService {
  VisitaDomiciliarResponse criar(VisitaDomiciliarRequest request);

  VisitaDomiciliarResponse atualizar(Long id, VisitaDomiciliarRequest request);

  void excluir(Long id);

  VisitaDomiciliarListaResponse listar();
}
