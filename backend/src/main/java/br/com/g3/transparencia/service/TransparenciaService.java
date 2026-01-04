package br.com.g3.transparencia.service;

import br.com.g3.transparencia.dto.TransparenciaListaResponse;
import br.com.g3.transparencia.dto.TransparenciaRequest;
import br.com.g3.transparencia.dto.TransparenciaResponse;

public interface TransparenciaService {
  TransparenciaListaResponse listar();

  TransparenciaResponse obter(Long id);

  TransparenciaResponse criar(TransparenciaRequest request);

  TransparenciaResponse atualizar(Long id, TransparenciaRequest request);

  void excluir(Long id);
}
