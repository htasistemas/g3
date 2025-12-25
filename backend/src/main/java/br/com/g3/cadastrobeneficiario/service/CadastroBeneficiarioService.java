package br.com.g3.cadastrobeneficiario.service;

import br.com.g3.cadastrobeneficiario.dto.CadastroBeneficiarioCriacaoRequest;
import br.com.g3.cadastrobeneficiario.dto.CadastroBeneficiarioResponse;
import java.util.List;

public interface CadastroBeneficiarioService {
  CadastroBeneficiarioResponse criar(CadastroBeneficiarioCriacaoRequest request);

  CadastroBeneficiarioResponse atualizar(Long id, CadastroBeneficiarioCriacaoRequest request);

  CadastroBeneficiarioResponse buscarPorId(Long id);

  List<CadastroBeneficiarioResponse> listar(String nome);

  void remover(Long id);
}
