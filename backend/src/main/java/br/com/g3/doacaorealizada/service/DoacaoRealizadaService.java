package br.com.g3.doacaorealizada.service;

import br.com.g3.doacaorealizada.dto.DoacaoRealizadaRequest;
import br.com.g3.doacaorealizada.dto.DoacaoRealizadaResponse;
import java.util.List;

public interface DoacaoRealizadaService {
  DoacaoRealizadaResponse criar(DoacaoRealizadaRequest request);

  DoacaoRealizadaResponse buscarPorId(Long id);

  List<DoacaoRealizadaResponse> listar();
}
