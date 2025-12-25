package br.com.g3.usuario.service;

import br.com.g3.usuario.dto.UsuarioAtualizacaoRequest;
import br.com.g3.usuario.dto.UsuarioCriacaoRequest;
import br.com.g3.usuario.dto.UsuarioResponse;
import java.util.List;

public interface UsuarioService {
  List<UsuarioResponse> listar();

  UsuarioResponse criar(UsuarioCriacaoRequest request);

  UsuarioResponse atualizar(Long id, UsuarioAtualizacaoRequest request);

  void remover(Long id);
}
