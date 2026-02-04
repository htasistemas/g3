package br.com.g3.tarefaspendencias.service;

import br.com.g3.tarefaspendencias.dto.TarefaPendenciaRequest;
import br.com.g3.tarefaspendencias.dto.TarefaPendenciaResponse;
import java.util.List;

public interface TarefaPendenciaService {
  List<TarefaPendenciaResponse> listar();

  TarefaPendenciaResponse buscarPorId(Long id);

  TarefaPendenciaResponse criar(TarefaPendenciaRequest request);

  TarefaPendenciaResponse atualizar(Long id, TarefaPendenciaRequest request);

  TarefaPendenciaResponse adicionarHistorico(Long id, String mensagem);

  void remover(Long id);
}
