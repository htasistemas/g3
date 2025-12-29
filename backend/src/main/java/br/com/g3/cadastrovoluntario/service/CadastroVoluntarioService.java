package br.com.g3.cadastrovoluntario.service;

import br.com.g3.cadastrovoluntario.dto.CadastroVoluntarioCriacaoRequest;
import br.com.g3.cadastrovoluntario.dto.CadastroVoluntarioResponse;
import java.util.List;

public interface CadastroVoluntarioService {
  CadastroVoluntarioResponse criar(CadastroVoluntarioCriacaoRequest request);

  CadastroVoluntarioResponse atualizar(Long id, CadastroVoluntarioCriacaoRequest request);

  CadastroVoluntarioResponse buscarPorId(Long id);

  List<CadastroVoluntarioResponse> listar();

  void remover(Long id);
}
