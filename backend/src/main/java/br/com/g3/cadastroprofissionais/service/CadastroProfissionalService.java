package br.com.g3.cadastroprofissionais.service;

import br.com.g3.cadastroprofissionais.dto.CadastroProfissionalCriacaoRequest;
import br.com.g3.cadastroprofissionais.dto.CadastroProfissionalResponse;
import java.util.List;

public interface CadastroProfissionalService {
  CadastroProfissionalResponse criar(CadastroProfissionalCriacaoRequest request);

  CadastroProfissionalResponse atualizar(Long id, CadastroProfissionalCriacaoRequest request);

  CadastroProfissionalResponse buscarPorId(Long id);

  List<CadastroProfissionalResponse> listar(String nome);

  void remover(Long id);
}
