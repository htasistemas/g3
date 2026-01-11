package br.com.g3.bancoempregos.service;

import br.com.g3.bancoempregos.dto.BancoEmpregoCandidatoRequest;
import br.com.g3.bancoempregos.dto.BancoEmpregoCandidatoResponse;
import java.util.List;

public interface BancoEmpregoCandidatoService {
  List<BancoEmpregoCandidatoResponse> listar(Long empregoId);

  BancoEmpregoCandidatoResponse criar(Long empregoId, BancoEmpregoCandidatoRequest request);

  void remover(Long id);
}
