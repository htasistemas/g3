package br.com.g3.oficios.service;

import br.com.g3.oficios.dto.OficioListaResponse;
import br.com.g3.oficios.dto.OficioRequest;
import br.com.g3.oficios.dto.OficioResponse;

public interface OficioService {
  OficioListaResponse listar();

  OficioResponse obter(Long id);

  OficioResponse criar(OficioRequest request);

  OficioResponse atualizar(Long id, OficioRequest request);

  void excluir(Long id);
}
