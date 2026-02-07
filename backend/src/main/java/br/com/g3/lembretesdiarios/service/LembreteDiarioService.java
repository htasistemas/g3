package br.com.g3.lembretesdiarios.service;

import br.com.g3.lembretesdiarios.dto.LembreteDiarioAdiarRequest;
import br.com.g3.lembretesdiarios.dto.LembreteDiarioRequest;
import br.com.g3.lembretesdiarios.dto.LembreteDiarioResponse;
import java.util.List;

public interface LembreteDiarioService {
  List<LembreteDiarioResponse> listar(Long usuarioId);

  LembreteDiarioResponse criar(LembreteDiarioRequest request);

  LembreteDiarioResponse atualizar(Long id, LembreteDiarioRequest request);

  LembreteDiarioResponse concluir(Long id);

  LembreteDiarioResponse adiar(Long id, LembreteDiarioAdiarRequest request);

  void excluir(Long id);
}
