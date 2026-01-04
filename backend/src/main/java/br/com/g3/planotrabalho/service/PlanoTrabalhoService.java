package br.com.g3.planotrabalho.service;

import br.com.g3.planotrabalho.dto.PlanoTrabalhoListaResponse;
import br.com.g3.planotrabalho.dto.PlanoTrabalhoRequest;
import br.com.g3.planotrabalho.dto.PlanoTrabalhoResponse;

public interface PlanoTrabalhoService {
  PlanoTrabalhoListaResponse listar();

  PlanoTrabalhoResponse obter(Long id);

  PlanoTrabalhoResponse criar(PlanoTrabalhoRequest request);

  PlanoTrabalhoResponse atualizar(Long id, PlanoTrabalhoRequest request);

  void excluir(Long id);
}
