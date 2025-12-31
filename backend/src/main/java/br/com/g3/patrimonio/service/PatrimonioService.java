package br.com.g3.patrimonio.service;

import br.com.g3.patrimonio.dto.PatrimonioMovimentoRequest;
import br.com.g3.patrimonio.dto.PatrimonioRequest;
import br.com.g3.patrimonio.dto.PatrimonioResponse;
import java.util.List;

public interface PatrimonioService {
  List<PatrimonioResponse> listar();

  PatrimonioResponse criar(PatrimonioRequest request);

  PatrimonioResponse atualizar(Long id, PatrimonioRequest request);

  PatrimonioResponse registrarMovimento(Long id, PatrimonioMovimentoRequest request);
}
