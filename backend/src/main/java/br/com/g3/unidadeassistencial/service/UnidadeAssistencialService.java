package br.com.g3.unidadeassistencial.service;

import br.com.g3.unidadeassistencial.dto.UnidadeAssistencialCriacaoRequest;
import br.com.g3.unidadeassistencial.dto.UnidadeAssistencialResponse;
import java.util.List;

public interface UnidadeAssistencialService {
  UnidadeAssistencialResponse criar(UnidadeAssistencialCriacaoRequest request);

  List<UnidadeAssistencialResponse> listar();

  UnidadeAssistencialResponse obterAtual();

  UnidadeAssistencialResponse atualizar(Long id, UnidadeAssistencialCriacaoRequest request);

  UnidadeAssistencialResponse geocodificarEndereco(Long id, boolean forcar);

  void remover(Long id);
}
