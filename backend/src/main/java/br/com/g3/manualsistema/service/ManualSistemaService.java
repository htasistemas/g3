package br.com.g3.manualsistema.service;

import br.com.g3.manualsistema.dto.ManualSistemaMudancaListaResponse;
import br.com.g3.manualsistema.dto.ManualSistemaMudancaRequest;
import br.com.g3.manualsistema.dto.ManualSistemaMudancaResponse;
import br.com.g3.manualsistema.dto.ManualSistemaResumoResponse;
import br.com.g3.manualsistema.dto.ManualSistemaSecaoResponse;

public interface ManualSistemaService {
  ManualSistemaResumoResponse obterResumo();

  ManualSistemaSecaoResponse obterSecao(String slug);

  ManualSistemaMudancaListaResponse listarMudancas(int limite);

  ManualSistemaMudancaResponse registrarMudanca(ManualSistemaMudancaRequest request);
}
