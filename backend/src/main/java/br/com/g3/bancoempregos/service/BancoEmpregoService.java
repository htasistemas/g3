package br.com.g3.bancoempregos.service;

import br.com.g3.bancoempregos.dto.BancoEmpregoRequest;
import br.com.g3.bancoempregos.dto.BancoEmpregoResponse;
import java.util.List;

public interface BancoEmpregoService {
  List<BancoEmpregoResponse> listar();

  BancoEmpregoResponse buscarPorId(Long id);

  BancoEmpregoResponse criar(BancoEmpregoRequest request);

  BancoEmpregoResponse atualizar(Long id, BancoEmpregoRequest request);

  void remover(Long id);
}
