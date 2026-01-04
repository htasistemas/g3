package br.com.g3.termofomento.service;

import br.com.g3.termofomento.dto.TermoFomentoAditivoRequest;
import br.com.g3.termofomento.dto.TermoFomentoResponse;
import br.com.g3.termofomento.dto.TermoFomentoRequest;
import java.util.List;

public interface TermoFomentoService {
  List<TermoFomentoResponse> listar();

  TermoFomentoResponse obter(Long id);

  TermoFomentoResponse criar(TermoFomentoRequest request);

  TermoFomentoResponse atualizar(Long id, TermoFomentoRequest request);

  TermoFomentoResponse adicionarAditivo(Long id, TermoFomentoAditivoRequest request);

  void excluir(Long id);
}
