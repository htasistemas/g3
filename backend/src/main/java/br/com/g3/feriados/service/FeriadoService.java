package br.com.g3.feriados.service;

import br.com.g3.feriados.dto.FeriadoRequest;
import br.com.g3.feriados.dto.FeriadoResponse;
import java.util.List;

public interface FeriadoService {
  List<FeriadoResponse> listar();

  FeriadoResponse criar(FeriadoRequest request);

  FeriadoResponse atualizar(Long id, FeriadoRequest request);

  void excluir(Long id);
}
