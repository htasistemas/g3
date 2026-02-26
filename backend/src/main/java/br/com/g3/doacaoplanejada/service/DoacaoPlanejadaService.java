package br.com.g3.doacaoplanejada.service;

import br.com.g3.doacaoplanejada.dto.DoacaoPlanejadaRequest;
import br.com.g3.doacaoplanejada.dto.DoacaoPlanejadaResponse;
import java.util.List;

public interface DoacaoPlanejadaService {
  DoacaoPlanejadaResponse criar(DoacaoPlanejadaRequest request);

  DoacaoPlanejadaResponse atualizar(Long id, DoacaoPlanejadaRequest request);

  DoacaoPlanejadaResponse buscarPorId(Long id);

  List<DoacaoPlanejadaResponse> listar();

  List<DoacaoPlanejadaResponse> listarPorBeneficiario(Long beneficiarioId);

  List<DoacaoPlanejadaResponse> listarPorVinculoFamiliar(Long vinculoFamiliarId);

  void remover(Long id);
}
